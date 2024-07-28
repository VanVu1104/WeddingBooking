package com.example.Quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Model.Dish;
import com.example.doandiamond.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class SuaXoaActivity extends AppCompatActivity {
    private EditText editTextName, editTextDescription, editTextPrice, editTextCategory, editTextImage;
    private Button btnUpdate, btnDelete;
    private FirebaseFirestore db;
    private String dishId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_xoa);

        // Ánh xạ các thành phần
        editTextName = findViewById(R.id.edit_text_name);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPrice = findViewById(R.id.edit_text_price);
        editTextCategory = findViewById(R.id.edit_text_category);
        editTextImage = findViewById(R.id.edit_text_image);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        dishId = intent.getStringExtra("id");
        String name = intent.getStringExtra("ten");
        String description = intent.getStringExtra("mota");
        int price = intent.getIntExtra("price", 0);
        String image = intent.getStringExtra("anh");
        String category = intent.getStringExtra("loai");

        // Đặt dữ liệu lên các view
        editTextName.setText(name);
        editTextDescription.setText(description);
        editTextPrice.setText(String.valueOf(price));
        editTextCategory.setText(category);
        editTextImage.setText(image);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDish();
            }
        });
    }

    private void updateDish() {
        String name = editTextName.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String image = editTextImage.getText().toString().trim();

        if (name.isEmpty() || priceString.isEmpty() || category.isEmpty() || image.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phải là một số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        Dish dish = new Dish(category, image, description, name, price);
        db.collection("AllDish").document(dishId)
                .set(dish)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SuaXoaActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(SuaXoaActivity.this, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show());
    }

    private void deleteDish() {
        db.collection("AllDish").document(dishId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SuaXoaActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(SuaXoaActivity.this, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show());
    }
}
