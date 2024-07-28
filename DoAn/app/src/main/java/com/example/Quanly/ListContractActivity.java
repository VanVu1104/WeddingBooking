package com.example.Quanly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.Adapter.BookingHistoryAdapter;
import com.example.Adapter.ListContractAdapter;
import com.example.Model.Contract;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListContractActivity extends AppCompatActivity implements ListContractAdapter.ProductCallback {
    RecyclerView rvListContract;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    List<Contract> contractList;
    ListContractAdapter contractAdapter;
    ImageView imv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contract);

        imv_back=findViewById(R.id.imv_back);
        rvListContract=findViewById(R.id.rvListContract);
        contractList=new ArrayList<>();
        rvListContract.setLayoutManager(new LinearLayoutManager(this));
        contractAdapter=new ListContractAdapter(contractList,this,this);
        rvListContract.setAdapter(contractAdapter);
        LoadListContract();
        imv_back.setOnClickListener(v -> finish());
    }
    private void LoadListContract() {
        db.collection("hopdong")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            contractList.clear();
                            for (DocumentSnapshot document : task.getResult()) {
                                String status = document.getString("status");
                                if(!"success".equals(status)){
                                    String name = document.getString("Tenkh");
                                    String phone = document.getString("sdt");
                                    String cate = document.getString("loaisanh");
                                    String date = document.getString("date");
                                    String time = document.getString("selectedTime");
                                    int mount = document.getLong("soban").intValue();
                                    int price_room = document.getLong("giasanh").intValue();
                                    int price_service = document.getLong("giadichvu").intValue();
                                    int price_menu = document.getLong("giathucdon").intValue();
                                    int total = document.getLong("tongtien").intValue();
                                    String documentId=document.getId();
                                    String Idyc=document.getString("Idyc");

                                    Contract contract = new Contract(name, phone, cate, date, time, mount, price_room, price_service, price_menu, total,status,documentId,Idyc);
                                    contractList.add(contract);
                                }
                            }
                            contractAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    protected void onResume() {
        super.onResume();
        LoadListContract();
    }

    @Override
    public void onItemClick(String tenkh, String sdt, String loaisanh, String date, int slb, String selectedTime, int giasanh, int giatd, int giadv, int tongtien,String documentId,String Idyc) {
        Intent i=new Intent(ListContractActivity.this,ContractPaymentActivity.class);
        i.putExtra("name",tenkh);
        i.putExtra("phone",sdt);
        i.putExtra("loaisanh",loaisanh);
        i.putExtra("ngay",date);
        i.putExtra("slb",slb);
        i.putExtra("time",selectedTime);
        i.putExtra("giasanh",giasanh);
        i.putExtra("giatd",giatd);
        i.putExtra("giadv",giadv);
        i.putExtra("tongtien",tongtien);
        i.putExtra("documentId",documentId);
        i.putExtra("Idyc",Idyc);
        startActivity(i);
    }
}