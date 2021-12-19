package com.hasan.mahasiswa.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class EditActivity extends AppCompatActivity {
    private int xId;
    private String xNama, xAlamat;
    private EditText etNama, etAlamat;
    private Button btnEdit;
    private String nama, alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent getValue = getIntent();
        xId = getValue.getIntExtra("xId", -1);
        xNama = getValue.getStringExtra("xNama");
        xAlamat = getValue.getStringExtra("xAlamat");

        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        btnEdit = findViewById(R.id.btn_edit);

        etNama.setText(xNama);
        etAlamat.setText(xAlamat);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama = etNama.getText().toString();
                alamat= etAlamat.getText().toString();

                updateData();
            }
        });
    }

    private void updateData() {
        APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
        Call<ResponseModel> changeData = ardData.ardUpdateData(xId, nama, alamat);

        changeData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int code = response.body().getCode();
                String message = response.body().getMessage();

//                Toast.makeText(EditActivity.this, "Code: " + code+ " | message " +message, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Toast.makeText(EditActivity.this, "Failed to reach the server " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
