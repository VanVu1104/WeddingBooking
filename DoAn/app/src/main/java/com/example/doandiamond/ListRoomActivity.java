package com.example.doandiamond;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.CategoryAdapter;
import com.example.Adapter.RoomAdapter;
import com.example.Model.Room;
import com.example.dichvu.DichVuActivity;
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

public class ListRoomActivity extends AppCompatActivity implements RoomAdapter.ProductCallback{
    private RecyclerView recyclerView;
    private RoomAdapter adapter;
    private List<Room> roomList;
    private FirebaseFirestore firestore;;
    NavigationView nav_mn;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
        firestore = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rc_room);
        roomList = new ArrayList<>();
        adapter = new RoomAdapter(roomList, this,this);
        recyclerView.setAdapter(adapter);
        loadDataFromFirestore();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.dr_mn);
        nav_mn = findViewById(R.id.nav_mn);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_mn.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mn_ycdt) {
                    startActivity(new Intent(ListRoomActivity.this, DattiecActivity.class));
                } else if (id == R.id.mn_dichvu) {
                    startActivity(new Intent(ListRoomActivity.this,DichVuActivity.class));
                } else if (id == R.id.mn_xacnhan) {
                    startActivity(new Intent(ListRoomActivity.this, MainActivity.class));
                } else if (id == R.id.mn_sanh) {
                    startActivity(new Intent(ListRoomActivity.this, ListRoomActivity.class));
                } else if (id == R.id.mn_thucdon) {
                    startActivity(new Intent(ListRoomActivity.this, ThucDonActivity.class));
                } else if (id == R.id.mn_updatett) {
                    startActivity(new Intent(ListRoomActivity.this, CapnhatActivity.class));
                } else if (id == R.id.mn_dn) {
                    startActivity(new Intent(ListRoomActivity.this, loginsdtActivity.class));
                } else if (id == R.id.mn_logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ListRoomActivity.this, dndkActivity.class));
                } else {
                    return false;
                }

                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
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
                                roomList.add(room);
                            }
                            if (roomList.isEmpty()) {
                                // Hiển thị thông báo hoặc xử lý giao diện khi danh sách trống
                                Toast.makeText(ListRoomActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                            // Thông báo cho adapter rằng tập dữ liệu đã thay đổi
                            adapter.notifyDataSetChanged();
                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(ListRoomActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onItemClick(String ten, String anh, String mota, int gia, String id, int sl) {
        Intent intent = new Intent(ListRoomActivity.this, DetailRoomActivity.class);
        intent.putExtra("name",ten);
        intent.putExtra("description", mota);
        intent.putExtra("gia", gia);
        intent.putExtra("anh", anh);
        intent.putExtra("sl", sl);
        startActivity(intent);

    }
}