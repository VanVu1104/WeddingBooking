package com.example.dichvu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ThemRoomActivity extends AppCompatActivity {
    EditText tenroom, anhroom, giaroom, motaroom, slroom;
    Button add;
    FirebaseFirestore fb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_them_room);
        fb = FirebaseFirestore.getInstance();
        anhroom=findViewById(R.id.anhroom);
        tenroom=findViewById(R.id.tenroom);
        giaroom=findViewById(R.id.giaroom);
        motaroom=findViewById(R.id.motaroom);
        slroom=findViewById(R.id.slroom);
        add=findViewById(R.id.taoroom);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themsanh();
                Intent i=new Intent(ThemRoomActivity.this, RoomAdminActivity
                        .class);
                startActivity(i);
            }
        });
    }
    private void themsanh() {
        String anh= anhroom.getText().toString();
        String ten= tenroom.getText().toString();
        int gia= Integer.parseInt(giaroom.getText().toString());
        String mota= motaroom.getText().toString();
        int sl= Integer.parseInt(slroom.getText().toString());
        Map<String, Object> sanhmoi = new HashMap<>();
        sanhmoi.put("anh",anh);
        sanhmoi.put("ten", ten);
        sanhmoi.put("gia",gia);
        sanhmoi.put("mota", mota);
        sanhmoi.put("sl", sl);
        fb.collection("Room").add(sanhmoi).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DattiecActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                Toast.makeText(ThemRoomActivity.this, "Thêm sảnh thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DattiecActivity", "Error adding document", e);
                Toast.makeText(ThemRoomActivity.this, "Tạo sảnh thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}