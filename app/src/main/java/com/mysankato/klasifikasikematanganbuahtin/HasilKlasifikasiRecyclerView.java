package com.mysankato.klasifikasikematanganbuahtin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysankato.klasifikasikematanganbuahtin.DataModel.HasilKlasifikasi;
import com.mysankato.klasifikasikematanganbuahtin.DataModel.ImageConverter;
import com.mysankato.klasifikasikematanganbuahtin.DataModel.RoomDB;

import java.util.List;

public class HasilKlasifikasiRecyclerView extends RecyclerView.Adapter<HasilKlasifikasiRecyclerView.HasilKlasifikasiViewHolder> {

    private List<HasilKlasifikasi> dataList;
    private Context context;
    private RoomDB database;

    public HasilKlasifikasiRecyclerView (Context context, List<HasilKlasifikasi> dataList) {
        this.context = context;
        this.dataList = dataList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HasilKlasifikasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new HasilKlasifikasiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HasilKlasifikasiViewHolder holder, int position) {
        HasilKlasifikasi data = dataList.get(position);
        database = RoomDB.getInstance(context);
        holder.imageView.setImageBitmap(ImageConverter.convertByteArray2Image(data.getFigImage()));
        holder.hasil.setText(data.getResult());
        holder.time.setText(data.getTime());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize Main data
                HasilKlasifikasi d = dataList.get(holder.getAdapterPosition());
                //Delete data from database
                database.hasilKlasifikasiDao().deleteHasilKlasifikasi(d);
                //Notify when data is deleted
                int position = holder.getAdapterPosition();
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class HasilKlasifikasiViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView hasil, time;
        ImageButton deleteBtn;
        public HasilKlasifikasiViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardImage);
            hasil = itemView.findViewById(R.id.cardName);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            time = itemView.findViewById(R.id.classifyTime);
        }
    }
}
