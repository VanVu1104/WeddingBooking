package com.example.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Model.DichVu;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

public class ServiceConfirmAdapter extends RecyclerView.Adapter<ServiceConfirmAdapter.ViewHolder> {
    private Context context;
    private List<DichVu> list;
    private FirebaseFirestore firestore;

    public ServiceConfirmAdapter(List<DichVu> list, Context context) {
        this.context = context;
        this.list = list;
        this.firestore = FirebaseFirestore.getInstance(); // Khởi tạo Firestore
    }

    @NonNull
    @Override
    public ServiceConfirmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServiceConfirmAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceConfirmAdapter.ViewHolder holder, int position) {
        DichVu item = list.get(position);
        holder.name.setText(item.getTen1());
        String formattedPrice = formatPrice(item.getGia1());
        holder.price.setText(formattedPrice);
        Glide.with(context).load(item.getAnh1()).into(holder.imageView);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuItemId = item.getId();
                    firestore.collection("Service").document(menuItemId)
                            .delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        int currentPosition = holder.getAdapterPosition();
                                        if (currentPosition != RecyclerView.NO_POSITION) {
                                            list.remove(currentPosition);
                                            notifyItemRemoved(currentPosition);
                                            notifyItemRangeChanged(currentPosition, list.size());
                                            // Gửi broadcast khi một mục đã được xóa thành công
                                            Intent intent = new Intent("CartItemDeleted");
                                            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                                        }
                                    } else {
                                        // Xử lý khi xóa không thành công
                                        Log.e("FirestoreError", "Error deleting document", task.getException());
                                        Toast.makeText(context, "Failed to delete item: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi gặp lỗi
                                    Log.e("FirestoreError", "Error deleting document", e);
                                    Toast.makeText(context, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Xử lý khi gặp lỗi
                                    Log.e("FirestoreError", "Error deleting document", e);
                                    Toast.makeText(context, "Failed to delete item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;
        ImageButton deleteButton; // Nút xóa

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvServiceNameItem);
            price = itemView.findViewById(R.id.tvServicetPrice);
            imageView = itemView.findViewById(R.id.ivServicetItem);
            deleteButton=itemView.findViewById(R.id.btnXoaSV);

        }
    }

    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}