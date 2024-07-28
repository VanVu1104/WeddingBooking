package com.example;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doandiamond.MainActivity;
import com.example.doandiamond.R;
import com.example.doandiamond.mapsActivity;

public class LocationActivity extends AppCompatActivity {

    LinearLayout location_1, phone_1;
    ImageView map_cn1, ic_back;
    TextView txt_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        txt_phone = findViewById(R.id.txt_phone);
        location_1 = findViewById(R.id.location_1);
        location_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, mapsActivity.class);
                startActivity(intent);
            }
        });
        map_cn1 = findViewById(R.id.map_cn1);
        map_cn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, mapsActivity.class);
                startActivity(intent);
            }
        });
        phone_1 = findViewById(R.id.phone_1);
        phone_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtphone = txt_phone.getText().toString();
                if (!TextUtils.isEmpty(txtphone)) {
                    String dial = "tel:" + txtphone;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }
                else {
                    Toast.makeText(LocationActivity.this, "abc", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ic_back = findViewById(R.id.ic_back);
        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}