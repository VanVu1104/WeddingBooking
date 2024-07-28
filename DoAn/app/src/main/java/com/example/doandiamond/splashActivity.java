package com.example.doandiamond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.Fireb;
import com.example.loginsdtActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(splashActivity.this, MainActivity.class);
                    Toast.makeText(splashActivity.this,"Chào mừng bạn đã quay lại",Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(splashActivity.this, dndkActivity.class);
                    startActivity(intent);
                }


                finish();
            }
        }, SPLASH_DELAY);
    }
}

