package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandiamond.R;

import java.util.List;

public class RoomUsageAdapter extends RecyclerView.Adapter<RoomUsageAdapter.ViewHolder> {

    private Context context;
    private List<RoomUsage> roomUsageList;

    public RoomUsageAdapter(Context context, List<RoomUsage> roomUsageList) {
        this.context = context;
        this.roomUsageList = roomUsageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_usage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomUsage roomUsage = roomUsageList.get(position);
        holder.textViewRoomName.setText(roomUsage.getLoaisanh());
        holder.textViewRoomUsageCount.setText(roomUsage.getSolan() + " lần");
    }

    @Override
    public int getItemCount() {
        return roomUsageList.size();
    }

    public void updateData(List<RoomUsage> roomUsageList) {
        this.roomUsageList = roomUsageList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoomName;
        TextView textViewRoomUsageCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewRoomUsageCount = itemView.findViewById(R.id.textViewRoomUsageCount);
        }
    }
}
