package com.example.doandiamond;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.CategoryAdapter;
import com.example.Model.Category;
import com.example.dichvu.DichVuActivity;
import com.example.loginsdtActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ThucDonActivity extends AppCompatActivity implements CategoryAdapter.ProductCallback {

    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    List<Category> categoryList;
    FirebaseFirestore firestore;
    ImageView back;
    String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thuc_don);

        firestore = FirebaseFirestore.getInstance();

        back=findViewById(R.id.imgback);
        recyclerView = findViewById(R.id.rvCate);
        Intent i=getIntent();
        boolean b =i.getBooleanExtra("show",false);
        admin=i.getStringExtra("admin_id");
        GridLayoutManager gridLayoutManager= new GridLayoutManager(ThucDonActivity.this,2);
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, this,this, b,admin);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(categoryAdapter);
        loadCategories();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadCategories() {
        firestore.collection("Category")
                .orderBy("id")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            categoryList.clear(); // Xóa danh sách trước khi thêm dữ liệu mới
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                categoryList.add(category);
                            }
                            if (categoryList.isEmpty()) {
                                // Hiển thị thông báo hoặc xử lý giao diện khi danh sách trống
                                Toast.makeText(ThucDonActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                            // Thông báo cho adapter rằng tập dữ liệu đã thay đổi
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(ThucDonActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(String ten, String cate, String anh,boolean b,String admin) {
        Intent i = new Intent(this, AllDishActivity.class);
        i.putExtra("ten", ten);
        i.putExtra("loai", cate);
        i.putExtra("anh", anh);
        i.putExtra("admin_id",admin);
        i.putExtra("show menu",b);
        startActivity(i);
    }
}
