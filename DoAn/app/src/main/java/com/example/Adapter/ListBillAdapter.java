package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Model.Contract;
import com.example.doandiamond.R;

import java.text.DecimalFormat;
import java.util.List;

public class ListBillAdapter extends RecyclerView.Adapter<ListBillAdapter.ViewHolder> {
    private List<Contract> contractList;
    private Context context;
    public ListBillAdapter(List<Contract> contractList, Context context) {
        this.contractList = contractList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new ListBillAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListBillAdapter.ViewHolder holder, int position) {
        Contract contract = contractList.get(position);
        holder.tenkh.setText(contract.getName());
        holder.sdt.setText(contract.getPhone());
        holder.date.setText(contract.getDate());
        holder.selectedTime.setText(contract.getTime());
        holder.total.setText(formatPrice(contract.getTotal()));
    }
    @Override
    public int getItemCount()
    {
        return contractList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tenkh, sdt, date, selectedTime,total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenkh = itemView.findViewById(R.id.name_bill);
            sdt = itemView.findViewById(R.id.phone_bill);
            date = itemView.findViewById(R.id.date_bill);
            selectedTime = itemView.findViewById(R.id.selectedTime_bill);
            total= itemView.findViewById(R.id.total_bill);
        }
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}
