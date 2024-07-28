package com.example.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Model.Booking;
import com.example.Model.Contract;
import com.example.Quanly.ContractPaymentActivity;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class ListContractAdapter  extends RecyclerView.Adapter<ListContractAdapter.ListContractViewHolder>{
    private List<Contract> list;
    private Context context;
    private ProductCallback productCallback;
    public ListContractAdapter(List<Contract> list, Context context, ProductCallback productCallback) {
        this.list = list;
        this.context = context;
        this.productCallback=productCallback;
    }

    @NonNull
    @Override
    public ListContractAdapter.ListContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listcontract, parent, false);
        return new ListContractAdapter.ListContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListContractAdapter.ListContractViewHolder holder, int position) {
        Contract contract = list.get(position);
        holder.tenkh.setText(contract.getName());
        holder.sdt.setText(contract.getPhone());
        holder.loaisanh.setText(contract.getCate());
        holder.date.setText(contract.getDate());
        holder.slb.setText(String.valueOf(contract.getMount()));
        holder.selectedTime.setText(contract.getTime());
        holder.giasanh.setText(formatPrice(contract.getPrice_room()));
        holder.giadichvu.setText(formatPrice(contract.getPrice_service()));
        holder.giathucdon.setText(formatPrice(contract.getPrice_menu()));
        holder.tongtien.setText(formatPrice(contract.getTotal()));
        String status=contract.getStatus();
       if("paid".equals(status)){
            holder.status_contract.setText(" Đã thanh toán tiền cọc ");
            holder.btncontract.setVisibility(View.VISIBLE);
            holder.btncontract.setOnClickListener(view -> productCallback.onItemClick(contract.getName(),contract.getPhone(),contract.getCate(),contract.getDate(),contract.getMount(),contract.getTime(),contract.getPrice_room(),contract.getPrice_menu(),contract.getPrice_service(),contract.getTotal(),contract.getDocumentId(),contract.getIdyc()));
            holder.btndeposit_contract.setVisibility(View.GONE);
       }
        else {
            holder.status_contract.setText(" Chưa thanh toán tiền cọc");
            holder.btncontract.setVisibility(View.GONE);
            holder.btndeposit_contract.setVisibility(View.VISIBLE);
            holder.btndeposit_contract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        String documentId=contract.getDocumentId();
                        PayDeposit(documentId, adapterPosition,contract.getIdyc(),holder);
                    }
                }
            });
        }
    }
    public interface ProductCallback{
        void onItemClick(String tenkh,String sdt,String loaisanh,String date, int slb,String selectedTime, int giasanh,int giatd,int giadv,int tongtien,String documentId,String Idyc);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ListContractViewHolder extends RecyclerView.ViewHolder {
        TextView tenkh, sdt, loaisanh, date, slb, selectedTime,giasanh,giathucdon,giadichvu,tongtien,status_contract;
        AppCompatButton btncontract,btndeposit_contract;
        public ListContractViewHolder(@NonNull View itemView) {
            super(itemView);
            tenkh=itemView.findViewById(R.id.tenkhlc);
            sdt=itemView.findViewById(R.id.sdtlc);
            loaisanh=itemView.findViewById(R.id.loaisanhlc);
            date=itemView.findViewById(R.id.datelc);
            slb=itemView.findViewById(R.id.slb);
            selectedTime=itemView.findViewById(R.id.selectedTimelc);
            giasanh=itemView.findViewById(R.id.giasanh);
            giadichvu=itemView.findViewById(R.id.giaDV);
            giathucdon=itemView.findViewById(R.id.giatd);
            tongtien=itemView.findViewById(R.id.tongtien);
            status_contract=itemView.findViewById(R.id.status_contract);
            btncontract=itemView.findViewById(R.id.btncontract);
            btndeposit_contract=itemView.findViewById(R.id.btndeposit_contract);
        }
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
        private void PayDeposit(String documentId,int position,String Idyc,ListContractViewHolder holder){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (documentId != null && !documentId.isEmpty()) {
            db.collection("hopdong")
                    .whereEqualTo("Idyc",Idyc)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    db.collection("hopdong").document(document.getId()).update("status", "paid")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Xác nhận tiền cọc thành công", Toast.LENGTH_SHORT).show();
                                                    db.collection("tiec").document(Idyc).update("status","paid")
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Cập nhật trạng thái trong danh sách và thông báo cho adapter
                                                            list.get(position).setStatus("paid");
                                                            holder.status_contract.setText(" Đã thanh toán tiền cọc ");
                                                            holder.btndeposit_contract.setVisibility(View.GONE);
                                                            holder.btncontract.setVisibility(View.VISIBLE);
                                                            notifyItemChanged(position);
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Thanh toán tiền cọc thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(context, "Không tìm thấy hợp đồng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Lỗi khi truy vấn Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(context, "Document ID không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
}
