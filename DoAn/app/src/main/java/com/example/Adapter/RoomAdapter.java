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
import com.example.Model.Room;
import com.example.doandiamond.R;


import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    List<Room> roomList;
    ProductCallback productCallBack;
    Context context;

    public RoomAdapter(List<Room> roomList, Context context, RoomAdapter.ProductCallback productCallBack) {
        this.roomList = roomList;
        this.productCallBack=productCallBack;
        this.context=context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoomAdapter.RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RoomAdapter.RoomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.nameTextView.setText(room.getTen());
        Glide.with(context)
                .load(room.getAnh())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(view -> productCallBack.onItemClick(room.getTen(),room.getAnh(), room.getMota(), room.getGia(),room.getDocumentId(), room.getSl()));
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public  class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.room_image);
            nameTextView = itemView.findViewById(R.id.room_name);
        }
    }
    public interface ProductCallback {
        void onItemClick(String ten,String anh, String mota, int gia, String id , int sl);
    }
}
