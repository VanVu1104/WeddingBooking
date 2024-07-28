package com.example.doandiamond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Quanly.dangnhapActivity;
import com.example.loginsdtActivity;

public class dndkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dndk);
        Button btn_DK,btndn;
            btn_DK = findViewById(R.id.btn_DK);
            btn_DK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(dndkActivity.this, dangnhapActivity.class);
                    startActivity(intent);
                }
            });
            btndn = findViewById(R.id.btn_DN);
            btndn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(dndkActivity.this,MainActivity.class);
                    Toast.makeText(dndkActivity.this,"Đăng nhập với tư cách là khách",Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            });

        }
    }
