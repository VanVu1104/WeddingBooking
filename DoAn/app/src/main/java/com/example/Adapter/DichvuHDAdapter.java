package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Model.DichVu;
import com.example.doandiamond.R;

import java.util.List;

public class DichvuHDAdapter extends RecyclerView.Adapter<DichvuHDAdapter.ViewHolder> {

    private List<DichVu> dichVuListList;
    private Context context;

    public DichvuHDAdapter(List<DichVu> dichVuListList, Context context) {
        this.dichVuListList = dichVuListList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dichvuhopdong, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DichVu dichVu = dichVuListList.get(position);
        holder.tvDishName.setText(dichVu.getTen1());
        holder.tvDishPrice.setText(String.valueOf(dichVu.getGia1()));

        // Load image using Glide or Picasso
        Glide.with(holder.imgDish.getContext()).load(dichVu.getAnh1()).into(holder.imgDish);
    }
    public int getTotalPrice1() {
        int totalPrice1 = 0;
        for (DichVu dichVu : dichVuListList) {
            totalPrice1 += dichVu.getGia1();
        }
        return totalPrice1;
    }

    @Override
    public int getItemCount() {
        return dichVuListList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName,  tvDishPrice;
        ImageView imgDish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishName = itemView.findViewById(R.id.tvtendv);
            tvDishPrice = itemView.findViewById(R.id.tvgiadv);
            imgDish = itemView.findViewById(R.id.ivanhdv);
        }
    }
}

