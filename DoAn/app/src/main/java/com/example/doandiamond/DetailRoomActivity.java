package com.example.doandiamond;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dichvu.DetailDVActivity;
import com.example.dichvu.DichVuActivity;

public class DetailRoomActivity extends AppCompatActivity {
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_room);

        ImageView imageView = findViewById(R.id.detail_image);
        TextView nameTextView = findViewById(R.id.detail_name);
        TextView descriptionTextView = findViewById(R.id.detail_description);
        TextView slTextView = findViewById(R.id.detail_sl);
        TextView giaTextView = findViewById(R.id.detail_gia);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        int gia = intent.getIntExtra("gia", 0);
        String anh = intent.getStringExtra("anh");
        int sl=intent.getIntExtra("sl",0);
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        giaTextView.setText(String.valueOf(gia)+"VND/bàn");
        slTextView.setText("Số lượng khách: "+String.valueOf(sl)+ " người");
        Glide.with(this).load(anh).into(imageView);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}