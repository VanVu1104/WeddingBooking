package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Model.Booking;
import com.example.doandiamond.R;

import java.text.DecimalFormat;
import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingHistoryViewHolder>{
    private List<Booking> bookingList;
    private Context context;
    private ProductCallback productCallback;
    public BookingHistoryAdapter(List<Booking> bookingList, Context context,ProductCallback productCallback) {
        this.bookingList = bookingList;
        this.context = context;
        this.productCallback=productCallback;
    }

    @NonNull
    @Override
    public BookingHistoryAdapter.BookingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new BookingHistoryAdapter.BookingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingHistoryAdapter.BookingHistoryViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tenkh.setText(booking.getName());
        holder.sdt.setText(booking.getPhone());
        holder.loaisanh.setText(booking.getCate_room());
        holder.date.setText(booking.getDate());
        holder.slk.setText(String.valueOf(booking.getSlk()));
        holder.selectedTime.setText(booking.getSelectedTime());
        holder.deposit.setText(formatPrice(booking.getDeposit()));
        String status= booking.getStatus();
        if("confirmed".equals(status)){
            holder.status.setText(" Đã được xác nhận ");
        }
        else if("canceled".equals(status)){
            holder.status.setText("Đã hủy");
            holder.deposit.setText("0 VNĐ");
        }
        else if("created".equals(status)) {
            holder.status.setText(" Đã tạo hợp đồng ");
            holder.btnpay.setVisibility(View.VISIBLE);
            holder.btnpay.setOnClickListener(view -> productCallback.onItemClick(booking.getName(),booking.getPhone(),booking.getCate_room(),booking.getSlk(),booking.getSelectedTime(),booking.getStatus(),booking.getDeposit(),booking.getDocumentId(),booking.getDate()));
        }
        else if("paid".equals(status)){
            holder.status.setText(" Đã thanh toán tiền cọc ");
            holder.btnpay.setVisibility(View.GONE);
        } else if("success".equals(status)){
            holder.status.setText("Hợp đồng hoàn thành");
            holder.btnpay.setVisibility(View.GONE);
        }
        else {
            holder.status.setText(" Chưa được duyệt ");
            holder.btnpay.setVisibility(View.GONE);
            holder.deposit.setText("0 VNĐ");
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tenkh, sdt, loaisanh, date, slk, selectedTime,status,deposit;
        AppCompatButton btnpay;
        public BookingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tenkh = itemView.findViewById(R.id.name_cus);
            sdt = itemView.findViewById(R.id.phone_cus);
            loaisanh = itemView.findViewById(R.id.cate_room);
            date = itemView.findViewById(R.id.datebook);
            slk = itemView.findViewById(R.id.khach);
            selectedTime = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            btnpay = itemView.findViewById(R.id.btnpay);
            deposit = itemView.findViewById(R.id.deposit);
        }
    }
    public interface ProductCallback{
        void onItemClick(String name, String phone, String cate_room, String slk, String selectedTime, String status, int deposit, String documentId,String date);
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
//    private void PayDeposit(String documentId,int position){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        if (documentId != null && !documentId.isEmpty()) {
//            db.collection("hopdong").whereEqualTo("Idyc", documentId)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
//                                for (DocumentSnapshot document : task.getResult()) {
//                                    db.collection("hopdong").document(document.getId()).update("status", "paid")
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(context, "Thanh toán tiền cọc thành công", Toast.LENGTH_SHORT).show();
//                                                    db.collection("tiec").document(documentId).update("status","paid")
//                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//                                                            // Cập nhật trạng thái trong danh sách và thông báo cho adapter
//                                                            bookingList.get(position).put("status", "paid");
//                                                            notifyItemChanged(position);
//                                                        }
//                                                    });
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(context, "Thanh toán tiền cọc thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }
//                            } else {
//                                Toast.makeText(context, "Không tìm thấy hợp đồng", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, "Lỗi khi truy vấn Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            Toast.makeText(context, "Document ID không hợp lệ", Toast.LENGTH_SHORT).show();
//        }
//    }
}
