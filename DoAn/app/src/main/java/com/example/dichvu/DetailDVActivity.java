package com.example.dichvu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Model.DichVu;
import com.example.doandiamond.DetailDishActivity;
import com.example.doandiamond.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DetailDVActivity extends AppCompatActivity {
    DichVu dichVu = new DichVu();
    FirebaseUser auth;
    String user;
    ImageView back;
    FirebaseFirestore firestores = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dvactivity);

        ImageView imageView = findViewById(R.id.detail_image1);
        TextView nameTextView = findViewById(R.id.detail_name2);
        TextView txtmota = findViewById(R.id.detail_mota2);
        TextView giaTextView = findViewById(R.id.detail_gia2);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        Button btnaddsv = findViewById(R.id.btn_addservice);
        boolean b=getIntent().getBooleanExtra("show",false);
        String admin=getIntent().getStringExtra("admin_id");
        Intent intent = getIntent();
        String ten1 = intent.getStringExtra("ten1");
        int gia1 = intent.getIntExtra("gia1", 0);
        String formatprice= formatPrice(gia1);
        String anh1 = intent.getStringExtra("anh1");
        String mota=intent.getStringExtra("mota1");
        txtmota.setText(mota);
        nameTextView.setText(ten1);
        giaTextView.setText(formatprice);
        Glide.with(this).load(anh1).into(imageView);
        dichVu.setTen1(ten1);
        dichVu.setGia1(gia1);
        dichVu.setAnh1(anh1);
        if(b)
        {
            btnaddsv.setVisibility(View.VISIBLE);
        }
        btnaddsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth!=null){
                user=auth.getUid();
                AddService();
            }
                else if(admin!=null){
                    user=admin;
                    AddService();
                }
                else {
                    Toast.makeText(DetailDVActivity.this,"Vui lòng đăng nhập để thêm dịch vụ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });
    }

    private void AddService() {
        firestores.collection("Service").whereEqualTo("image",dichVu.getAnh1()).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(task.getResult().isEmpty()){
                            Map<String, Object> serviceData = new HashMap<>();
                            serviceData.put("name1", dichVu.getTen1());
                            serviceData.put("price1", dichVu.getGia1());
                            serviceData.put("image1", dichVu.getAnh1());
                            serviceData.put("userID", user);
                            firestores.collection("Service")
                                    .add(serviceData)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("AddService", "Dịch vụ đã được thêm với ID: " + documentReference.getId());
                                        Toast.makeText(DetailDVActivity.this, "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(DetailDVActivity.this, "Lỗi khi thêm dịch vụ", Toast.LENGTH_SHORT).show();

                                        Log.e("AddService", "Lỗi khi thêm dịch vụ", e);
                                    });
                        }else {
                            // Matching document found, dish already exists
                            Toast.makeText(DetailDVActivity.this, "Dịch vụ đã tồn tại trong thực đơn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}