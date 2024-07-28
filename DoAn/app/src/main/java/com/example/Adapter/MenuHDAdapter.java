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
import com.example.Model.Dish;
import com.example.doandiamond.R;

import java.util.List;

public class MenuHDAdapter extends RecyclerView.Adapter<MenuHDAdapter.ViewHolder> {

    private List<Dish> dishList;
    private Context context;

    public MenuHDAdapter(List<Dish> dishList, Context context) {
        this.dishList = dishList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menuhopdong, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.tvDishName.setText(dish.getName());
        holder.tvDishPrice.setText(String.valueOf(dish.getPrice()));

        // Load image using Glide or Picasso
        Glide.with(holder.imgDish.getContext()).load(dish.getImage()).into(holder.imgDish);
    }
    public int getTotalPrice() {
        int totalPrice = 0;
        for (Dish dish : dishList) {
            totalPrice += dish.getPrice();
        }
        return totalPrice;
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDishName,  tvDishPrice;
        ImageView imgDish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDishName = itemView.findViewById(R.id.tvtenmenu);
            tvDishPrice = itemView.findViewById(R.id.tvgiamenu);
            imgDish = itemView.findViewById(R.id.ivanhmenu);
        }
    }
}

