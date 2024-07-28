package com.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.user;
import com.example.doandiamond.DattiecActivity;
import com.example.doandiamond.ListRoomActivity;
import com.example.doandiamond.MainActivity;
import com.example.doandiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class loginActivity extends AppCompatActivity {
    private EditText username,mkk;
    Button hoanthanh;
    ProgressBar progressBar;
    String phone;

    user userr;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.login_username);
        hoanthanh = findViewById(R.id.login_let_me_in_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        phone = getIntent().getExtras().getString("phone");
        getuser();
        hoanthanh.setOnClickListener((v -> {
            setUsername();
        }));
    }
    @SuppressLint("RestrictedApi")
    private void setUsername() {

        String ten = username.getText().toString();
        if(ten.isEmpty() || ten.length() < 5){
            username.setError("Tên đăng nhập trên 5 kí tự ");
            return;
        }

        setInProgress((true));
        if(userr!=null) {
            userr.setTen(ten);
        }else{
            userr = new user(phone,ten,Fireb.currentUserId());
        }
        Fireb.currentUserDetails().set(userr).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    Toast.makeText(loginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(intent);
                }
            }
        });
    }

    private void getuser() {
        setInProgress(true);
        Fireb.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInProgress(false);
                if(task.isSuccessful()){
                    userr =    task.getResult().toObject(user.class);
                    if(userr!=null){
                        username.setText(userr.getTen());

                    }
                }
            }
        });
    }
    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            hoanthanh.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            hoanthanh.setVisibility(View.VISIBLE);
        }
    }
}