package com.example.dichvu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.Adapter.DichVuAdapter;
import com.example.Model.DichVu;
import com.example.doandiamond.CapnhatActivity;
import com.example.doandiamond.DattiecActivity;
import com.example.doandiamond.ListRoomActivity;
import com.example.doandiamond.MainActivity;
import com.example.doandiamond.MenuConfirmActivity;
import com.example.doandiamond.R;
import com.example.doandiamond.ThucDonActivity;
import com.example.doandiamond.dndkActivity;
import com.example.loginsdtActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DichVuActivity extends AppCompatActivity implements DichVuAdapter.ProductCallback{
    private RecyclerView recyclerView;
    private DichVuAdapter dichVuAdapter;
    private List<DichVu> dichvuList;
    private FirebaseFirestore firestore;
    ImageView imvback;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dich_vu);
        firestore = FirebaseFirestore.getInstance();
        Intent i=getIntent();
        boolean b =i.getBooleanExtra("show",false);
        String admin=i.getStringExtra("admin_id");
        imvback=findViewById(R.id.imvback);
        recyclerView = findViewById(R.id.rvDV);
        dichvuList = new ArrayList<>();
        dichVuAdapter = new DichVuAdapter(dichvuList, this,this,b,admin);
        recyclerView.setAdapter(dichVuAdapter);
        loadDichVu();
        imvback.setOnClickListener(v -> finish());
    }

    private void loadDichVu() {
        firestore.collection("diuyn")  // Đảm bảo rằng đây là collection hợp lệ
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Xóa danh sách trước khi thêm dữ liệu mới
                            dichvuList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DichVu dv = document.toObject(DichVu.class);
                                dichvuList.add(dv);
                            }
                            dichVuAdapter.notifyDataSetChanged();
                            if (dichvuList.isEmpty()) {
                                // Hiển thị thông báo hoặc xử lý giao diện khi danh sách trống
                                Toast.makeText(DichVuActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                            // Thông báo cho adapter rằng tập dữ liệu đã thay đổi

                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(DichVuActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    @Override
    public void onItemClick(String ten1, String anh1, int gia1, String mota1,boolean b,String admin) {
        Intent i = new Intent(DichVuActivity.this, DetailDVActivity.class);
        i.putExtra("anh1", anh1);
        i.putExtra("ten1", ten1);
        i.putExtra("gia1", gia1);
        i.putExtra("mota1", mota1);
        i.putExtra("show",b);
        i.putExtra("admin_id",admin);
        startActivity(i);
    }
}