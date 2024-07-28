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

import java.text.DecimalFormat;
import java.util.List;

public class QuanLySanPhamAdapter extends RecyclerView.Adapter<QuanLySanPhamAdapter.ViewHolder> {
    private Context context;
    private List<Dish> list;
    ProductCallback productCallBack;

    public QuanLySanPhamAdapter(Context context, List<Dish> list, ProductCallback productCallBack) {
        this.context = context;
        this.list = list;
        this.productCallBack = productCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dishitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish item = list.get(position);
        holder.name.setText(item.getName());
        String formattedPrice = formatPrice(item.getPrice());
        holder.price.setText(formattedPrice);

        Glide.with(context).load(item.getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(view -> productCallBack.ItemClick(item.getName(), item.getPrice(), item.getMota(), item.getImage(),item.getCategory()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imDish);
            name = itemView.findViewById(R.id.txtname);
            price = itemView.findViewById(R.id.txtprice);
        }
    }

    public interface ProductCallback {
        void ItemClick(String ten, int price, String mota, String anh,String cate);
    }

    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}
