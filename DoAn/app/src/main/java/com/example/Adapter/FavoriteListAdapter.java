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

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder> {
    private Context context;
    private List<Dish> list;
    private ProductCallback2 productCallback;
    private boolean b;

    public FavoriteListAdapter(List<Dish> list, Context context, ProductCallback2 productCallback, boolean b) {
        this.context = context;
        this.list = list;
        this.productCallback = productCallback;
        this.b = b;
    }

    @NonNull
    @Override
    public FavoriteListAdapter.FavoriteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dishitem, parent, false);
        return new FavoriteListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteListViewHolder holder, int position) {
        Dish item = list.get(position);
        holder.name.setText(item.getName());
        String formattedPrice = formatPrice(item.getPrice());
        holder.price.setText(formattedPrice);

        Glide.with(context).load(item.getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                productCallback.onItemClick2(item.getName(), item.getPrice(), item.getMota(), item.getImage(), item.getCategory(), b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class FavoriteListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;

        public FavoriteListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imDish);
            name = itemView.findViewById(R.id.txtname);
            price = itemView.findViewById(R.id.txtprice);
        }
    }

    public interface ProductCallback2 {
        void onItemClick2(String ten, int price, String mota, String anh, String cate, boolean b);
    }

    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}
