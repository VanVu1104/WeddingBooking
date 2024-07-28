package com.example.doandiamond;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dichvu.DichVuActivity;
import com.example.loginsdtActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CapnhatActivity extends AppCompatActivity {

    TextView mtvProUserName, mtvProUserMail, mtvProUserPhone,txtten;
    EditText edtProUserName, edtProUserMail, edtProUserPhone;
    ImageView mimgProfileImage;
    EditText edtprofileuserDate;
    NavigationView nav_mn;
    DrawerLayout drawerLayout;

    Button btChangeInfoProfile, btChangePassProfile;
    ImageButton btnUserback, imgImage;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    FirebaseUser user = firebaseAuth.getCurrentUser();
    String doan12 ;
    boolean verification;
    private boolean isResendClicked = false;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capnhat);

        //btnUserback = findViewById(R.id.btnUserBack);

        //btnUserback.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
        //        finish();
        //}
        // });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.dr_mn);
        nav_mn = findViewById(R.id.nav_mn);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_mn.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mn_ycdt) {
                    startActivity(new Intent(CapnhatActivity.this,DattiecActivity.class));
                } else if (id == R.id.mn_dichvu) {
                    startActivity(new Intent(CapnhatActivity.this,DichVuActivity.class));
                }else if (id == R.id.mn_history) {
                    startActivity(new Intent(CapnhatActivity.this,BookingHistoryActivity.class));
                } else if (id == R.id.mn_xacnhan) {
                    startActivity(new Intent(CapnhatActivity.this, MainActivity.class));
                } else if (id == R.id.mn_sanh) {
                    startActivity(new Intent(CapnhatActivity.this, ListRoomActivity.class));
                } else if (id == R.id.mn_thucdon) {
                    startActivity(new Intent(CapnhatActivity.this, ThucDonActivity.class));
                } else if (id == R.id.mn_updatett) {
                    startActivity(new Intent(CapnhatActivity.this, CapnhatActivity.class));
                } else if (id == R.id.mn_dn) {
                    startActivity(new Intent(CapnhatActivity.this, loginsdtActivity.class));
                } else if (id == R.id.mn_logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(CapnhatActivity.this, dndkActivity.class));
                } else {
                    return false;
                }

                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });
        edtProUserName = findViewById(R.id.edtProfileUserName);
        edtProUserMail = findViewById(R.id.edtProfileUserMail);
        edtProUserPhone = findViewById(R.id.edtProfileUserPhone);
        if (user == null) {
            Toast.makeText(CapnhatActivity.this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            doan12 = user.getUid();
        }
        DocumentReference InfoProfiledocumentReference = firestore.collection("users").document(doan12);
        InfoProfiledocumentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && value.exists()) {
                    edtProUserName.setText(value.getString("ten"));
                    edtProUserMail.setText(value.getString("diachi"));
                    edtProUserPhone.setText(value.getString("phone"));
                    edtprofileuserDate.setText(value.getString("nam"));
                }
            }
        });

        btChangeInfoProfile = findViewById(R.id.btnChaneInfoProfile);
        btChangeInfoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(CapnhatActivity.this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = edtProUserMail.getText().toString();
                String currentMail = user.getEmail();
                DocumentReference ChangeInfoDocumentReference = firestore.collection("users").document(doan12);
                Map<String,Object> edited = new HashMap<>();


                String fullName = edtProUserName.getText().toString();
                String phone = edtProUserPhone.getText().toString();
                String nam= edtprofileuserDate.getText().toString();
                if (!fullName.isEmpty() || !phone.isEmpty()) {
                    edited.put("ten", fullName);
                    edited.put("phone", phone);
                    edited.put("diachi",email);
                    edited.put("nam",nam);
                    ChangeInfoDocumentReference.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CapnhatActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CapnhatActivity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        edtprofileuserDate = findViewById(R.id.edtProfileUserDate);
        final Calendar calendar = Calendar.getInstance();
        final int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        final int thang = calendar.get(Calendar.MONTH);
        final int nam = calendar.get(Calendar.YEAR);

        edtprofileuserDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sm = new SimpleDateFormat("mm/dd/yyyy");
                String strDate = sm.format(new Date());
                DatePickerDialog dialog = new DatePickerDialog(CapnhatActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int Nam, int Thang, int Ngay) {

                        Thang = Thang + 1;
                        String date = Ngay+"/"+Thang+"/"+Nam;
                        edtprofileuserDate.setText(date);
                    }
                },nam,thang,ngay);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
                dialog.show();

            }
        });

    }

}