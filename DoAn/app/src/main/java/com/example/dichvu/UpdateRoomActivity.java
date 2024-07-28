package com.example.dichvu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandiamond.R;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateRoomActivity extends AppCompatActivity {
    EditText imageView;
    EditText name;
    EditText description;
    EditText gia;
    EditText sl;
    Button sua, xoa;
    String id1;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_room);
        imageView = findViewById(R.id.anhroom1);
        name = findViewById(R.id.tenroom1);
        description = findViewById(R.id.motaroom1);
        gia = findViewById(R.id.giaroom1);
        sl=findViewById(R.id.slroom1);
        sua=findViewById(R.id.suaroom);
        xoa=findViewById(R.id.xoaroom);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String name1 = intent.getStringExtra("name1");
        String description1 = intent.getStringExtra("description1");
        int gia1 = intent.getIntExtra("gia1", 0);
        int sl1 = intent.getIntExtra("sl1",0);
        String anh1 = intent.getStringExtra("anh1");
        id1=intent.getStringExtra("id1");
        imageView.setText(anh1);
        name.setText(name1);
        description.setText(description1);
        gia.setText(String.valueOf(gia1));
        sl.setText(String.valueOf(sl1));
        sua.setOnClickListener(v -> {
            String newAnh = imageView.getText().toString();
            String newName = name.getText().toString();
            String newDescription = description.getText().toString();
            int newGia = Integer.parseInt(gia.getText().toString());
            int newSl = Integer.parseInt(sl.getText().toString());
            updateRoomData(newAnh, newName, newDescription, newGia, newSl);
            Intent i=new Intent(UpdateRoomActivity.this, RoomAdminActivity.class
            );
            startActivity(i);
        });
        xoa.setOnClickListener(v -> {
            deleteRoomData();
            Intent i=new Intent(UpdateRoomActivity.this, RoomAdminActivity
                    .class);
            startActivity(i);
        });
    }
    private void deleteRoomData() {
        db.collection("Room").document(id1)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateRoomActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại activity trước đó sau khi xóa
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateRoomActivity.this, "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void updateRoomData(String anh, String name, String description, int gia, int sl) {
        // Tạo một bản đồ để chứa dữ liệu cần cập nhật
        Map<String, Object> room = new HashMap<>();
        room.put("anh", anh);
        room.put("ten", name);
        room.put("mota", description);
        room.put("gia", gia);
        room.put("sl",sl);
        // Cập nhật dữ liệu trên Firestore
        db.collection("Room").document(id1)
                .set(room)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateRoomActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateRoomActivity.this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}