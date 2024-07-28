package com.example.Quanly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.dichvu.DichVuActivity;
import com.example.dichvu.RoomAdminActivity;
import com.example.doandiamond.CapnhatActivity;
import com.example.doandiamond.DattiecActivity;
import com.example.doandiamond.ListRoomActivity;
import com.example.doandiamond.MainActivity;
import com.example.doandiamond.R;
import com.example.doandiamond.ThucDonActivity;
import com.example.doandiamond.dndkActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainadminActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;

    NavigationView nav_admin2;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainadmin);

        floatingActionButton = findViewById(R.id.btnThongKe);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainadminActivity.this, StaticActivity.class);
            startActivity(intent);
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
                    startActivity(new Intent(MainadminActivity.this, RoomAdminActivity.class));
                } else if (id == R.id.mn_updmn) {
                    startActivity(new Intent(MainadminActivity.this, QuanLySanPhamActivity.class));
                } else if (id == R.id.mn_main) {
                    startActivity(new Intent(MainadminActivity.this, MainadminActivity.class));
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
}