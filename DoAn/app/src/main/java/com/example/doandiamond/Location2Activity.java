package com.example.doandiamond;

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

public class Location2Activity extends AppCompatActivity {

    LinearLayout location_2, phone_2;
    ImageView map_cn2, ic_back2;
    TextView txt_phone2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location2);
        txt_phone2 = findViewById(R.id.txt_phone2);
        location_2 = findViewById(R.id.location_2);
        location_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Location2Activity.this, maps2Activity.class);
                startActivity(intent);
            }
        });
        map_cn2 = findViewById(R.id.map_cn2);
        map_cn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Location2Activity.this, maps2Activity.class);
                startActivity(intent);
            }
        });
        phone_2 = findViewById(R.id.phone_2);
        phone_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtphone = txt_phone2.getText().toString();
                if (!TextUtils.isEmpty(txtphone)) {
                    String dial = "tel:" + txtphone;
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
                }
                else {
                    Toast.makeText(Location2Activity.this, "abc", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ic_back2 = findViewById(R.id.ic_back2);
        ic_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}