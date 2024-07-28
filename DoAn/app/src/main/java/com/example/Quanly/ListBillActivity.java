package com.example.Quanly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Adapter.BookingHistoryAdapter;
import com.example.Adapter.ListBillAdapter;
import com.example.Model.Contract;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListBillActivity extends AppCompatActivity {
    ListBillAdapter billAdapter;
    RecyclerView rvBill;
    ImageView imv_back;
    List<Contract> contractList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bill);
        init();
        contractList=new ArrayList<>();
        billAdapter = new ListBillAdapter(contractList, this);
        rvBill.setLayoutManager(new LinearLayoutManager(this));
        rvBill.setAdapter(billAdapter);
        LoadListBill();
        imv_back.setOnClickListener(v -> finish());
    }
    private void init(){
        rvBill= findViewById(R.id.rvBill);
        imv_back= findViewById(R.id.imv_bill);
    }
    private void LoadListBill(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("Contract_Payment")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            contractList.clear();
                            for(DocumentSnapshot documentSnapshot:task.getResult()){
                                String name=documentSnapshot.getString("name_contract");
                                String phone=documentSnapshot.getString("phone_contract");
                                int total=documentSnapshot.getLong("total").intValue();
                                String time=documentSnapshot.getString("selectedTime_contract");
                                String date=documentSnapshot.getString("date_contract");
                                Contract contract = new Contract(name,phone,date,time,total);
                                contractList.add(contract);
                            }
                            billAdapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ListBillActivity.this, "Không load được hóa đơn", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}