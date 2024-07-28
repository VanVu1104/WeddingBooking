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
import com.example.Model.Category;
import com.example.doandiamond.R;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    List<Category> categorylist;
    ProductCallback productCallBack;
    Context context;
    boolean b;
    String admin;
    public CategoryAdapter(List<Category> categorylist, Context context, ProductCallback productCallBack, boolean b,String admin) {
        this.productCallBack=productCallBack;
        this.context=context;
        this.categorylist = categorylist;
        this.b=b;
        this.admin=admin;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.thucdonitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category=categorylist.get(position);
        holder.txtname.setText(category.getName());
        Glide.with(context).load(category.getImage()).into(holder.img);
        holder.itemView.setOnClickListener(view -> productCallBack.onItemClick(category.getName(),category.getCate(),category.getImage(),b,admin));

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txtname,tvCateName;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imvCate);
            txtname=itemView.findViewById(R.id.tvname);

        }
    }
    public interface ProductCallback {

        void onItemClick(String ten, String cate, String anh,boolean b,String admin);
    }
}
