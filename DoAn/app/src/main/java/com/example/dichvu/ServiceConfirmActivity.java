package com.example.dichvu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Adapter.MenuConfirmAdapter;
import com.example.Adapter.ServiceConfirmAdapter;
import com.example.Model.DichVu;
import com.example.Model.Dish;
import com.example.doandiamond.DattiecActivity;
import com.example.doandiamond.MenuConfirmActivity;
import com.example.doandiamond.R;
import com.example.doandiamond.ThucDonActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServiceConfirmActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceConfirmAdapter serviceConfirmAdapter;
    private List<DichVu> dichVuList = new ArrayList<>();
    private CollectionReference servicelist = FirebaseFirestore.getInstance().collection("Service");
    private CollectionReference confirmedServiceList = FirebaseFirestore.getInstance().collection("Confirmed_Service");
    private Button btnLuu,btnthem;
    private String userID;
    private BroadcastReceiver cartItemDeletedReceiver;
    ImageView imvCart,btnsvBack;
    String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_confirm);

        recyclerView = findViewById(R.id.rvService);
        btnLuu = findViewById(R.id.btnservice);
        btnthem=findViewById(R.id.btnThemService);
        imvCart=findViewById(R.id.imvCartsv);
        btnsvBack=findViewById(R.id.btnsvBack);
        Intent i=getIntent();
        boolean b=i.getBooleanExtra("show",false);
        admin=i.getStringExtra("admin_id");
        userID = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (userID == null) {
            userID = admin;
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceConfirmAdapter = new ServiceConfirmAdapter(dichVuList, this);
        recyclerView.setAdapter(serviceConfirmAdapter);
        cartItemDeletedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Cập nhật trạng thái của giỏ hàng
                updateEmptyCartMessage();
            }
        };
        loadServiceFromFirestore();
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ServiceConfirmActivity.this, DichVuActivity.class);
                i.putExtra("show",b);
                i.putExtra("admin_id",userID);
                startActivity(i);
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveServiceToFirestore();
            }
        });
        btnsvBack.setOnClickListener(v -> finish());
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadServiceFromFirestore();
        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(cartItemDeletedReceiver, new IntentFilter("CartItemDeleted"));
        // Cập nhật thông báo giỏ hàng trống
        updateEmptyCartMessage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartItemDeletedReceiver);
    }

    private void loadServiceFromFirestore() {
        servicelist.whereEqualTo("userID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    dichVuList.clear(); // Xóa danh sách trước khi tải lại
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DichVu dichVu = document.toObject(DichVu.class);
                        dichVu.setId(document.getId());
                        dichVu.setAnh1(document.getString("image1"));
                        dichVu.setTen1(document.getString("name1"));
                        dichVu.setGia1((document.getLong("price1").intValue()));
                        dichVu.setMota1(document.getString("mota1"));
                        dichVuList.add(dichVu);
                    }
                    serviceConfirmAdapter.notifyDataSetChanged();
                    updateEmptyCartMessage();
                } else {
                    Toast.makeText(ServiceConfirmActivity.this, "Lỗi khi tải dịch vụ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void saveServiceToFirestore() {
        Map<String, Object> confirmedService = new HashMap<>();
        List<Map<String, Object>> services = new ArrayList<>();

        for (DichVu dichVu : dichVuList) {
            Map<String, Object> serviceData = new HashMap<>();
            serviceData.put("name", dichVu.getTen1());
            serviceData.put("price", dichVu.getGia1());
            serviceData.put("image", dichVu.getAnh1());
            serviceData.put("userID", userID);
            services.add(serviceData);
        }
        confirmedService.put("services", services);
        confirmedServiceList.add(confirmedService)
                .addOnSuccessListener(documentReference -> {
//                    Intent i=new Intent();
//                    i.putExtra("idservice",documentReference.getId());
//                     finish();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("idservice", documentReference.getId()); // Thay thế "your_menu_id" bằng ID thực tế
                    setResult(RESULT_OK, resultIntent);
                    clearService();
                    finish();
                    Toast.makeText(ServiceConfirmActivity.this, "Dịch vụ đã được lưu", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(ServiceConfirmActivity.this, "Lỗi khi lưu dịch vụ", Toast.LENGTH_SHORT).show());
    }
    private void clearService() {
        CollectionReference cart = servicelist;
        cart.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    doc.getReference().delete();
                }
            }
        });
    }
    private void updateEmptyCartMessage() {
        if (dichVuList.isEmpty()) {
            imvCart.setVisibility(View.VISIBLE);
            btnLuu.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            imvCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnLuu.setVisibility(View.VISIBLE);
        }
    }
}