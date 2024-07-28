package com.example.doandiamond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.LocationActivity;
import com.example.Quanly.AdminConfirmActivity;
import com.example.dichvu.DichVuActivity;
import com.example.loginsdtActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton btn_dr;
    ViewFlipper viewFlipper;
    LinearLayout cn1_main, cn2_main;
    ImageView cn1, cn2;
    Animation in, out;
    NavigationView nav_mn;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar nav
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
                    startActivity(new Intent(MainActivity.this, DattiecActivity.class));
                } else if (id == R.id.mn_dichvu) {
                    startActivity(new Intent(MainActivity.this,DichVuActivity.class));
                } else if (id == R.id.mn_xacnhan) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if (id == R.id.mn_sanh) {
                    startActivity(new Intent(MainActivity.this, ListRoomActivity.class));
                } else if (id == R.id.mn_thucdon) {
                    startActivity(new Intent(MainActivity.this, ThucDonActivity.class));
                } else if (id == R.id.mn_updatett) {
                    startActivity(new Intent(MainActivity.this, CapnhatActivity.class));
                }else if (id == R.id.mn_history) {
                    startActivity(new Intent(MainActivity.this, BookingHistoryActivity.class));
                } else if (id == R.id.mn_dn) {
                    startActivity(new Intent(MainActivity.this, loginsdtActivity.class));
                } else if (id == R.id.mn_logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, dndkActivity.class));
                } else {
                    return false;
                }

                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });

//        btn_dr = findViewById(R.id.btn_dr);
//        btn_dr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.openDrawer(nav_mn);
//            }
//        });

        viewFlipper = findViewById(R.id.vfl);
        in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        out = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        cn1 = findViewById(R.id.cn1);
        cn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });

        cn1_main = findViewById(R.id.cn1_main);
        cn1_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });

        cn2 = findViewById(R.id.cn2);
        cn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Location2Activity.class));
            }
        });

        cn2_main = findViewById(R.id.cn2_main);
        cn2_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Location2Activity.class));
            }
        });
    }
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
