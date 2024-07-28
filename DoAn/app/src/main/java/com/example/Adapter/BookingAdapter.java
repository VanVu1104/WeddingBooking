package com.example.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Quanly.HopdongActivity;
import com.example.doandiamond.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Map<String, Object>> bookingList;
    private Context context;

    public BookingAdapter(List<Map<String, Object>> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;

    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Map<String, Object> booking = bookingList.get(position);

        holder.tenkh.setText(" " + booking.get("Tenkh"));
        holder.sdt.setText(" " + booking.get("sdt"));
        holder.loaisanh.setText(" " + booking.get("loaisanh"));
        holder.date.setText("" + booking.get("date"));
        holder.slk.setText(" " + booking.get("slk"));
        holder.selectedTime.setText(" " + booking.get("selectedTime"));
        String status = (String) booking.get("status");

        // Ẩn nút "Tạo hợp đồng" ban đầu
        holder.createContractButton.setVisibility(View.GONE);
        if ("".equals(status)) {
            holder.confirmButton.setEnabled(true);
            holder.cancelButton.setEnabled(true);
            holder.confirmButton.setText("Xác Nhận");
            holder.cancelButton.setText("Hủy yêu cầu");
        } else if ("confirmed".equals(status) ) {
            holder.confirmButton.setEnabled(false);
            holder.cancelButton.setEnabled(false);
            holder.confirmButton.setText("Đã xác nhận");
            holder.cancelButton.setText("Đã xác nhận");
            // Hiển thị nút "Tạo hợp đồng" nếu đã xác nhận
            holder.createContractButton.setVisibility(View.VISIBLE);
        } else if("created".equals(status))
        {
            holder.confirmButton.setEnabled(false);
            holder.cancelButton.setEnabled(false);
            holder.createContractButton.setEnabled(false);
            holder.confirmButton.setText("Đã tạo hợp đồng");
            holder.cancelButton.setText("Đã tạo hợp đồng");
        }
        else if ("canceled".equals(status)) {
            holder.confirmButton.setEnabled(false);
            holder.cancelButton.setEnabled(false);
            holder.confirmButton.setText("Đã hủy");
            holder.cancelButton.setText("Đã hủy");
        } else {
            holder.confirmButton.setEnabled(true);
            holder.cancelButton.setEnabled(true);
            holder.confirmButton.setText("Xác Nhận");
            holder.cancelButton.setText("Hủy yêu cầu");
        }

        holder.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmBooking((String) booking.get("documentId"), holder);
            }
        });
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelBooking((String) booking.get("documentId"), holder);
            }
        });
        holder.createContractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createContract((String) booking.get("documentId"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    private void confirmBooking(String documentId, BookingViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tiec").document(documentId).update("status", "confirmed")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đơn đặt tiệc đã được xác nhận", Toast.LENGTH_SHORT).show();
                    holder.confirmButton.setEnabled(false);
                    holder.cancelButton.setEnabled(false);
                    holder.confirmButton.setText("Đã xác nhận");
                    holder.cancelButton.setText("Đã xác nhận");
                    // Hiển thị nút "Tạo hợp đồng" sau khi xác nhận
                    holder.createContractButton.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Xác nhận thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    private void cancelBooking(String documentId, BookingViewHolder holder) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tiec").document(documentId).update("status", "canceled")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đơn đặt tiệc đã được hủy", Toast.LENGTH_SHORT).show();
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        bookingList.remove(position);
                        notifyItemRemoved(position);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Hủy thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    private void createContract(String documentId) {
        Intent intent = new Intent(context, HopdongActivity.class);
        intent.putExtra("documentId", documentId);
        context.startActivity(intent);
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tenkh, sdt, loaisanh, date, slk, selectedTime;
        Button confirmButton, createContractButton, cancelButton;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tenkh = itemView.findViewById(R.id.tenkh);
            sdt = itemView.findViewById(R.id.sdt);
            loaisanh = itemView.findViewById(R.id.loaisanh);
            date = itemView.findViewById(R.id.date);
            slk = itemView.findViewById(R.id.slk);
            selectedTime = itemView.findViewById(R.id.selectedTime);
            confirmButton = itemView.findViewById(R.id.confirmButton);
            createContractButton = itemView.findViewById(R.id.taohopdong);
            cancelButton = itemView.findViewById(R.id.delButton);
        }
    }
}