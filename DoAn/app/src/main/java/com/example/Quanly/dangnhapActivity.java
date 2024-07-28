package com.example.Quanly;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Model.AdminUser;
import com.example.dichvu.RoomAdminActivity;
import com.example.doandiamond.MainActivity;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ResourceBundle;

public class dangnhapActivity extends AppCompatActivity {
    EditText tendn, mk;
    FirebaseAuth auth;
    Button btn;
    FirebaseFirestore db;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        tendn = findViewById(R.id.login_admin);
        mk = findViewById(R.id.mk_admin);
        btn = findViewById(R.id.btn_login_admin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dangnhap();
            }

            private void dangnhap() {
                String Name = tendn.getText().toString();
                String matkhau = mk.getText().toString();
                CollectionReference usersCollection = db.collection("Admin");
                usersCollection.whereEqualTo("Name", Name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String storedUsername = document.getString("Name");
                                        String storedPassword = document.getString("matkhau");
                                        String storedRole = document.getString("Role");
                                        String documentId = document.getId();

                                        if (storedUsername.equals(Name) && storedPassword.equals(matkhau)) {
                                            if (storedRole != null) {
                                                if (storedRole.equalsIgnoreCase("Dattiec")) {
                                                    Intent i = new Intent(dangnhapActivity.this, AdminConfirmActivity.class);
                                                   i.putExtra("admin_id",documentId);
                                                   Toast.makeText(dangnhapActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                                    startActivity(i);
                                                } else if (storedRole.equalsIgnoreCase("Dichvu")) {
                                                    Intent i = new Intent(dangnhapActivity.this, MainadminActivity.class);
                                                    startActivity(i);
                                                }
                                            } else {
                                                Log.d(TAG, "Không có chức vụ này ");
                                            }
                                            } else {
                                            Log.d(TAG, "Lỗi bị sai", task.getException());
                                        }
                                    }
                                }
                            }
                        });
            }
        });
    }
}