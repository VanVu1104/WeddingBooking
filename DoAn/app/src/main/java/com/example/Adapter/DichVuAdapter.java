package com.example.Adapter;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;


public class DichVuAdapter extends RecyclerView.Adapter<DichVuAdapter.DichVuViewHolder> {
    List<DichVu> dichvulist;
    DichVuAdapter.ProductCallback productCallBack;
    Context context;
    boolean b;
    String admin;
    public DichVuAdapter(List<DichVu> dichvulist, Context context, DichVuAdapter.ProductCallback productCallBack,boolean b,String admin) {
        this.productCallBack=productCallBack;
        this.context=context;
        this.dichvulist = dichvulist;
        this.b=b;
        this.admin=admin;
    }

    @NonNull
    @Override
    public DichVuAdapter.DichVuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DichVuAdapter.DichVuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DichVuAdapter.DichVuViewHolder holder, int position) {
        DichVu dichvu= dichvulist.get(position);
        holder.txtname1.setText(dichvu.getTen1());
        System.out.println("chay");
        Glide.with(context).load(dichvu.getAnh1()).into(holder.img1);
        holder.itemView.setOnClickListener(view -> productCallBack.onItemClick(dichvu.getTen1(),dichvu.getAnh1(),dichvu.getGia1(),dichvu.getMota1(),b,admin));

    }

    @Override
    public int getItemCount() {
        return dichvulist.size();
    }

    public class DichVuViewHolder extends RecyclerView.ViewHolder {
        ImageView img1;
        TextView txtname1;
        public DichVuViewHolder(@NonNull View itemView) {
            super(itemView);
            img1 = itemView.findViewById(R.id.imgDV);
            txtname1 = itemView.findViewById(R.id.txtnameDV);

        }
    }
    public interface ProductCallback {

        void onItemClick(String ten1, String anh1, int gia1, String mota,boolean b,String admin);
    }
}