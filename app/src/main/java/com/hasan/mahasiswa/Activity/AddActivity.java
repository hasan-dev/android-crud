package com.hasan.mahasiswa.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hasan.mahasiswa.API.APIRequestData;
import com.hasan.mahasiswa.API.RetroServer;
import com.hasan.mahasiswa.Model.ResponseModel;
import com.hasan.mahasiswa.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    private EditText etNama, etAlamat;
    private Button btnSubmit;
    private String nama, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        btnSubmit = findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                alamat = etAlamat.getText().toString();

                if(nama.trim().equals("")) {
                    etNama.setError("Nama harus diisi");
                } else if(alamat.trim().equals("")) {
                    etAlamat.setError("Alamat harus diisi");
                } else {
                    createData();
                }
            }
        });
    }

    public void createData() {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> saveData = ardData.ardCreateData(nama, alamat);

        saveData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();

//                Toast.makeText(AddActivity.this, "Code: " + code+ " | message " +message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Toast.makeText(AddActivity.this, "Failed to reach the server " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
