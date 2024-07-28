package com.example.Quanly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Dish;
import com.example.doandiamond.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UploadDishActivity extends AppCompatActivity {
    private EditText editTextName,editTextDescription,editTextPrice,editTextCategory,editTextImage;

    private Button btnThem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("AllDish");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_dish);

        //Ánh xạ
        editTextName = findViewById(R.id.edit_text_name);
        editTextPrice = findViewById(R.id.edit_text_price);
        editTextCategory = findViewById(R.id.edit_text_category);
        editTextImage = findViewById(R.id.edit_text_image);
        editTextDescription=findViewById(R.id.edit_text_description);
        btnThem=findViewById(R.id.btnThem);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
    }
    public void addNote() {
        String name = editTextName.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String category = editTextCategory.getText().toString().trim();
        String mota = editTextDescription.getText().toString().trim();
        String image = editTextImage.getText().toString().trim();

        if (name.isEmpty() || priceString.isEmpty() || category.isEmpty() || image.isEmpty() || mota.isEmpty()) {
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

        Dish note = new Dish(category, image, mota, name, price);
        notebookRef.add(note)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Sản phẩm đã được thêm", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextPrice.setText("");
                    editTextCategory.setText("");
                    editTextImage.setText("");
                    editTextDescription.setText("");
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                });
    }

}