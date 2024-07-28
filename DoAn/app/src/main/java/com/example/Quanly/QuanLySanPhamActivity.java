package com.example.Quanly;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.QuanLySanPhamAdapter;
import com.example.Model.Dish;
import com.example.dichvu.RoomAdminActivity;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class QuanLySanPhamActivity extends AppCompatActivity implements QuanLySanPhamAdapter.ProductCallback {
    private static final int REQUEST_CODE_ADD_DISH = 1;
    List<Dish> dishList = new ArrayList<>();
    QuanLySanPhamAdapter quanLySanPhamAdapter;
    FirebaseFirestore firestore;
    RecyclerView rvDish;
    FloatingActionButton fab;

    NavigationView nav_admin2;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        rvDish = findViewById(R.id.rvDish);
        fab = findViewById(R.id.fab);
        firestore = FirebaseFirestore.getInstance();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(QuanLySanPhamActivity.this, 3);
        quanLySanPhamAdapter = new QuanLySanPhamAdapter(this, dishList, this);
        rvDish.setLayoutManager(gridLayoutManager);
        rvDish.setAdapter(quanLySanPhamAdapter);

        LoadSanPham();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuanLySanPhamActivity.this, UploadDishActivity.class);
                startActivityForResult(i, REQUEST_CODE_ADD_DISH);
            }
        });
    }

    private void LoadSanPham() {
        firestore.collection("AllDish")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dishList.clear();
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                if (dish != null) {
                                    dish.setId(documentSnapshot.getId());
                                    dishList.add(dish);
                                }
                            }
                            quanLySanPhamAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(QuanLySanPhamActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.dr_mn);
        nav_admin2 = findViewById(R.id.nav_admin2);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_admin2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mn_updsanh) {
                    startActivity(new Intent(QuanLySanPhamActivity.this, RoomAdminActivity.class));
                } else if (id == R.id.mn_updmn) {
                    startActivity(new Intent(QuanLySanPhamActivity.this, QuanLySanPhamActivity.class));
                } else if (id == R.id.mn_main) {
                    startActivity(new Intent(QuanLySanPhamActivity.this, MainadminActivity.class));
//                } else if (id == R.id.mn_upddv) {
//                    startActivity(new Intent(MainadminActivity.this, MainadminActivity.class));
                } else {
                    return false;
                }
                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });
    }


    @Override
    public void ItemClick(String ten, int price, String mota, String anh, String cate) {
        Dish clickedDish = null;
        for (Dish dish : dishList) {
            if (dish.getName().equals(ten) && dish.getPrice() == price) {
                clickedDish = dish;
                break;
            }
        }

        if (clickedDish != null) {
            Intent i = new Intent(this, SuaXoaActivity.class);
            i.putExtra("id", clickedDish.getId());
            i.putExtra("ten", ten);
            i.putExtra("price", price);
            i.putExtra("mota", mota);
            i.putExtra("anh", anh);
            i.putExtra("loai", cate);
            startActivityForResult(i, REQUEST_CODE_ADD_DISH);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            LoadSanPham();
        }
    }
}
