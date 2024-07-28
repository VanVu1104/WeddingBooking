package com.example.dichvu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.RoomAdapter;
import com.example.Model.Room;

import com.example.Quanly.MainadminActivity;
import com.example.Quanly.QuanLySanPhamActivity;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RoomAdminActivity extends AppCompatActivity  implements RoomAdapter.ProductCallback{
    private RecyclerView recyclerView1;
    Button them;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private FirebaseFirestore firestore;
    NavigationView nav_admin2;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_admin);
        firestore = FirebaseFirestore.getInstance();
        them=findViewById(R.id.them);
        recyclerView1 =  findViewById(R.id.rc_room_admin);

        roomList = new ArrayList<>();
        adapter = new RoomAdapter(roomList, this,this);
        recyclerView1.setAdapter(adapter);
        loadDataFromFirestore();
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RoomAdminActivity.this, ThemRoomActivity.class);
                startActivity(intent);
            }
        });
    }
    private void loadDataFromFirestore() {
        firestore.collection("Room")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            roomList.clear(); // Xóa danh sách trước khi thêm dữ liệu mới
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Room room = document.toObject(Room.class);
                                room.setDocumentId(document.getId());
                                roomList.add(room);
                            }
                            if (roomList.isEmpty()) {
                                // Hiển thị thông báo hoặc xử lý giao diện khi danh sách trống
                                Toast.makeText(RoomAdminActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                            // Thông báo cho adapter rằng tập dữ liệu đã thay đổi
                            adapter.notifyDataSetChanged();
                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(RoomAdminActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
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
                    startActivity(new Intent(RoomAdminActivity.this, RoomAdminActivity.class));
                } else if (id == R.id.mn_updmn) {
                    startActivity(new Intent(RoomAdminActivity.this, QuanLySanPhamActivity.class));
                } else if (id == R.id.mn_main) {
                    startActivity(new Intent(RoomAdminActivity.this, MainadminActivity.class));
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
    public void onItemClick(String ten, String anh, String mota, int gia,String id, int sl1) {
        Intent intent = new Intent(RoomAdminActivity.this, UpdateRoomActivity.class);
        intent.putExtra("id1",id);
        intent.putExtra("name1",ten);
        intent.putExtra("description1", mota);
        intent.putExtra("gia1", gia);
        intent.putExtra("anh1", anh);
        intent.putExtra("sl1",sl1);
        startActivity(intent);


    }
}