package com.example.Quanly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Adapter.BookingAdapter;
import com.example.doandiamond.R;
import com.example.doandiamond.dndkActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdminConfirmActivity extends AppCompatActivity {
    private RecyclerView recyclerViewBookings;
    private BookingAdapter bookingAdapter;
    private List<Map<String, Object>> bookingList;
    private FirebaseFirestore db;
    NavigationView nav_admin;
    DrawerLayout drawerLayout;
    String admin;

    FloatingActionButton fab;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_confirm);

        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        fab=findViewById(R.id.addcontact);
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList, this);
        recyclerViewBookings.setAdapter(bookingAdapter);
        admin=getIntent().getStringExtra("admin_id");
        db = FirebaseFirestore.getInstance();

        loadBookings();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AdminConfirmActivity.this,AddContactActivity.class);
                i.putExtra("admin_id",admin);
                startActivity(i);

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.dr_mn);
        nav_admin = findViewById(R.id.nav_admin);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_admin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mn_xnyc) {
                    startActivity(new Intent(AdminConfirmActivity.this, AdminConfirmActivity.class));
                } else if (id == R.id.mn_taohd) {
                    startActivity(new Intent(AdminConfirmActivity.this, ListContractActivity.class));
                }else if(id == R.id.trangthai){
                    startActivity(new Intent(AdminConfirmActivity.this,TrangthaiSanhActivity.class));
                }else if (id == R.id.mn_logout) {
                    startActivity(new Intent(AdminConfirmActivity.this,dndkActivity.class));
                }else if (id == R.id.mn_static) {
                    startActivity(new Intent(AdminConfirmActivity.this,StaticActivity.class));
                }else if (id == R.id.mn_bill) {
                    startActivity(new Intent(AdminConfirmActivity.this,ListBillActivity.class));
                }
                else {
                    return false;
                }
                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });
    }
    protected void onResume(){
        super.onResume();
        loadBookings();
    }
    private void loadBookings() {
        db.collection("hopdong").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot hopdongSnapshot = task.getResult();
                        if (hopdongSnapshot != null) {
                            // Khai báo hopdongIdycSet là một HashSet
                            Set<String> hopdongIdycSet = new HashSet<>();
                            for (DocumentSnapshot document : hopdongSnapshot) {
                                String idyc = document.getString("Idyc");
                                if (idyc != null) {
                                    hopdongIdycSet.add(idyc);
                                }
                            }
                            db.collection("tiec")
                                    .orderBy("timestamp", Query.Direction.DESCENDING).get()
                                    .addOnCompleteListener(tiecTask -> {
                                        if (tiecTask.isSuccessful()) {
                                            QuerySnapshot tiecSnapshot = tiecTask.getResult();
                                            if (tiecSnapshot != null) {
                                                bookingList.clear();
                                                for (DocumentSnapshot document : tiecSnapshot) {
                                                    String documentId = document.getId();
                                                    String status = document.getString("status");
                                                    // Lọc bỏ các tài liệu có documentId trong hopdongIdycSet
                                                    if (!hopdongIdycSet.contains(documentId) && !"canceled".equals(status)) {
                                                        Map<String, Object> booking = document.getData();
                                                        assert booking != null;
                                                        booking.put("documentId", documentId); // Thêm ID tài liệu để sử dụng khi xác nhận
                                                        bookingList.add(booking);
                                                    }
                                                }
                                                bookingAdapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            Log.e("AdminConfirmActivity", "Error getting documents: ", tiecTask.getException());
                                        }
                                    });
                        }
                    } else {
                        Log.e("AdminConfirmActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}
