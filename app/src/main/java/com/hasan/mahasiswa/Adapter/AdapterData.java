package com.hasan.mahasiswa.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hasan.mahasiswa.API.APIRequestData;
import com.hasan.mahasiswa.API.RetroServer;
import com.hasan.mahasiswa.Activity.EditActivity;
import com.hasan.mahasiswa.Activity.MainActivity;
import com.hasan.mahasiswa.Model.DataModel;
import com.hasan.mahasiswa.Model.ResponseModel;
import com.hasan.mahasiswa.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {
    private Context context;
    private List<DataModel> listMahasiswa;
    private List<DataModel> listData;
    private int idLaundry;

    public AdapterData(Context context, List<DataModel> listMahasiswa) {
        this.context = context;
        this.listMahasiswa = listMahasiswa;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent,false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listMahasiswa.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNama.setText(dm.getNama());
        holder.tvAlamat.setText(dm.getAlamat());
    }

    @Override
    public int getItemCount() {
        return listMahasiswa.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvNama, tvAlamat;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat= itemView.findViewById(R.id.tv_alamat);

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder dialogMessage = new AlertDialog.Builder(context);
                    dialogMessage.setMessage("Pilih operasi yang dilakukan");
                    dialogMessage.setCancelable(true);

                    idLaundry = Integer.parseInt(tvId.getText().toString());

                    dialogMessage.setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData();
                            dialogInterface.dismiss();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainActivity) context).retrieveData();
                                }
                            }, 1000);
                        }
                    });

                    dialogMessage.setNegativeButton("Edit", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getData();
                        }
                    });
                    dialogMessage.show();

                    return false;
                }
            });
        }

        private void deleteData() {
            APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
            Call<ResponseModel> removeData = ardData.ardDeleteData(idLaundry);

            removeData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int code = response.body().getCode();
                    String message = response.body().getMessage();

                    Toast.makeText(context, "Code: " + code+ " | message " +message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Failed to reach the server " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void getData(){
            APIRequestData ardData = RetroServer.connectRetrofit().create(APIRequestData.class);
            Call<ResponseModel> takeData = ardData.ardGetData(idLaundry);

            takeData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int code = response.body().getCode();
                    String message = response.body().getMessage();
                    listData = response.body().getData();

                    int varIdMahasiswa= listData.get(0).getId();
                    String varNamaMahasiswa = listData.get(0).getNama();
                    String varAlamatMahasiswa = listData.get(0).getAlamat();

                    Intent sendValue = new Intent(context, EditActivity.class);
                    sendValue.putExtra("xId", varIdMahasiswa);
                    sendValue.putExtra("xNama", varNamaMahasiswa);
                    sendValue.putExtra("xAlamat", varAlamatMahasiswa);
                    context.startActivity(sendValue);

                    Toast.makeText(context, "Code: " + code+ " | message " +message+ " | Data: " +varIdMahasiswa+ " | " +varNamaMahasiswa+" | " +varAlamatMahasiswa, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(context, "Failed to reach the server " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

