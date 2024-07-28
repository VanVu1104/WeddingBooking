package com.example.Quanly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dichvu.ServiceConfirmActivity;
import com.example.doandiamond.DattiecActivity;
import com.example.doandiamond.MenuConfirmActivity;
import com.example.doandiamond.R;
import com.example.doandiamond.dndkActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {
    NavigationView nav_admin;
    DrawerLayout drawerLayout;
    private static final int REQUEST_CODE_THUC_DON = 1;
    private static final int REQUEST_CODE_SERVICE = 2;
    EditText name,phone,guest;
    TextView date;
    String edtSanh;
    Button themtd,themdv;
    Button submit;
    RadioGroup radioGroupp;
    String idmenu1;
    String idservice;
    FirebaseAuth auth;
    FirebaseFirestore fb;
    List<String> arraySanh;
    ArrayAdapter<String> spinnerA;
    Spinner spinnerSanh;
    String admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        init();
        admin=getIntent().getStringExtra("admin_id");
        final Calendar calendar = Calendar.getInstance();
        final int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        final int thang = calendar.get(Calendar.MONTH);
        final int nam = calendar.get(Calendar.YEAR);
        loadSanh();
        loadGioCuoiFromFirestore();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sm = new SimpleDateFormat("MM/dd/yyyy");
                String strDate = sm.format(new Date());
                DatePickerDialog dialog = new DatePickerDialog(AddContactActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int Nam, int Thang, int Ngay) {

                        Thang = Thang + 1;
                        String ngay = Ngay+"/"+Thang+"/"+Nam;
                        date.setText(ngay);
                    }
                },nam,thang,ngay);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }});
        arraySanh = new ArrayList<>();
        spinnerA = new ArrayAdapter<>(AddContactActivity.this, android.R.layout.simple_spinner_item,arraySanh);
        spinnerA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSanh.setAdapter(spinnerA);
        spinnerSanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtSanh =(String)  arraySanh.get(position);
//
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                edtSanh = null;
                Toast.makeText(AddContactActivity.this, "onNothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
        themtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AddContactActivity.this, MenuConfirmActivity.class);
                i.putExtra("show",true);
                i.putExtra("admin_id",admin);
                startActivityForResult(i,REQUEST_CODE_THUC_DON);
            }
        });
        themdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AddContactActivity.this, ServiceConfirmActivity.class);
                i.putExtra("show",true);
                i.putExtra("admin_id",admin);
                startActivityForResult(i,REQUEST_CODE_SERVICE);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiyeucau();
            }
        });

    }
    private void init(){
        auth = FirebaseAuth.getInstance();
        fb = FirebaseFirestore.getInstance();
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        themtd=findViewById(R.id.btnadminthemtd);
        spinnerSanh= findViewById(R.id.room);
        themdv=findViewById(R.id.btnadminthemdv);
        guest=findViewById(R.id.guest);
        submit=findViewById(R.id.admingui);
        radioGroupp = findViewById(R.id.adminradioGroup);
        date=findViewById(R.id.adminngay);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.dr_mn);
        nav_admin = findViewById(R.id.nav_admin);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav_admin.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.mn_xnyc) {
                    startActivity(new Intent(AddContactActivity.this, AdminConfirmActivity.class));
                } else if (id == R.id.mn_logout) {
                    startActivity(new Intent(AddContactActivity.this, dndkActivity.class));
                } else if (id == R.id.mn_taohd) {
                    startActivity(new Intent(AddContactActivity.this, HopdongActivity.class));
                }else if(id == R.id.trangthai){
                    startActivity(new Intent(AddContactActivity.this,TrangthaiSanhActivity.class));
                } else {
                    return false;
                }
                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });
    }
    private void loadSanh() {
        fb.collection("tiec1"). document("Sanh")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            arraySanh.clear(); // Xóa danh sách trước khi thêm dữ liệu mới

                            if (document.exists()) {
                                String item12 = document.getString("item12");
                                String item13 = document.getString("item13");
                                String item14 = document.getString("item14");
                                String item15 = document.getString("item15");
                                String item16 = document.getString("item16");
                                if (item12 != null) {
                                    arraySanh.add(item12);
                                }
                                if (item13 != null) {
                                    arraySanh.add(item13);
                                }
                                if (item14 != null) {
                                    arraySanh.add(item14);
                                }
                                if (item15 != null) {
                                    arraySanh.add(item15);
                                }
                                if (item16 != null) {
                                    arraySanh.add(item16);
                                }
                            }
                            if (arraySanh.isEmpty()) {
                                // Hiển thị thông báo hoặc xử lý giao diện khi danh sách trống
                                Toast.makeText(AddContactActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            } else {
                                // Cập nhật Adapter khi dữ liệu đã sẵn sàng
                                spinnerA.notifyDataSetChanged();
                            }
                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(AddContactActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void loadGioCuoiFromFirestore() {

        DocumentReference docRef = fb.collection("tiec1").document("giocuoi");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> gioCuoiList = (List<String>) document.get("timework");
                        if (gioCuoiList != null) {
                            Log.d("DattiecActivity", "Danh sách giờ cưới: " + gioCuoiList);
                            radioGroupp.removeAllViews();
                            for (String gioCuoi : gioCuoiList) {
                                RadioButton radioButton = new RadioButton(getApplicationContext());
                                radioButton.setText(gioCuoi);
                                radioGroupp.addView(radioButton);
                            }
                            radioGroupp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    RadioButton selectedRadioButton = findViewById(checkedId);
                                    String selectedTime = selectedRadioButton.getText().toString();
                                    Log.d("DattiecActivity", "Selected time: " + selectedTime);

                                    // Chỉ cập nhật giờ được chọn
//                                    fb.collection("cuoi").document(auth.getCurrentUser().getUid()).update("selectedTime", selectedTime)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(DattiecActivity.this, "Selected time: " + selectedTime, Toast.LENGTH_SHORT).show();
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Log.e("DattiecActivity", "Error updating document", e);
//                                                }
//                                            });
                                }
                            });
                        } else {
                            Log.e("DattiecActivity", "Danh sách giờ cưới bị null");
                        }
                    } else {
                        Log.e("DattiecActivity", "Tài liệu không tồn tại");
                    }
                } else {
                    Log.e("DattiecActivity", "Có lỗi xảy ra khi lấy tài liệu", task.getException());
                }
            }
        });
    }
    private void guiyeucau() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID;
        // Check if the user is logged in
        if (currentUser != null) {
             userID = currentUser.getUid();
        }
        else if(admin!=null){
             userID = admin;
        }
        else {
            Toast.makeText(AddContactActivity.this,"Vui lòng đăng nhập để đặt tiệc",Toast.LENGTH_SHORT).show();
            return;
        }

        String Tenkh = name.getText().toString().trim();
        String sdt = phone.getText().toString().trim();
        String loaisanh = edtSanh;
        String idmenu= idmenu1;
        String idsv=idservice;
        String datebook = date.getText().toString().trim();
        String slk = guest.getText().toString().trim();
        int selectedId = radioGroupp.getCheckedRadioButtonId();

        // Kiểm tra các trường không trống
        if (Tenkh.isEmpty()) {
            Toast.makeText(AddContactActivity.this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sdt.length() != 10) {
            Toast.makeText(AddContactActivity.this, "Số điện thoại phải đủ 10 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        if (loaisanh == null) {
            Toast.makeText(AddContactActivity.this, "Vui lòng chọn sảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idmenu == null) {
            Toast.makeText(AddContactActivity.this, "Vui lòng chọn thực đơn", Toast.LENGTH_SHORT).show();
            return;
        }
        if (datebook.isEmpty()) {
            Toast.makeText(AddContactActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            return;
        }
        if (slk.isEmpty()) {
            Toast.makeText(AddContactActivity.this, "Vui lòng nhập số lượng khách", Toast.LENGTH_SHORT).show();
            return;
        }
        int soLuongKhach;
        try {
            soLuongKhach = Integer.parseInt(slk);
        } catch (NumberFormatException e) {
            Toast.makeText(AddContactActivity.this, "Số lượng khách phải là một số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (soLuongKhach > 500) {
            Toast.makeText(AddContactActivity.this, "Số lượng khách không được vượt quá 500", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedId == -1) {
            Toast.makeText(AddContactActivity.this, "Vui lòng chọn giờ cưới", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedTime = selectedRadioButton.getText().toString();

        // Tạo bản ghi tiệc mới
        Map<String, Object> tiecmoi = new HashMap<>();
        tiecmoi.put("Userid", userID);
        tiecmoi.put("Tenkh", Tenkh);
        tiecmoi.put("sdt", sdt);
        tiecmoi.put("loaisanh", loaisanh);
        tiecmoi.put("date", datebook);
        tiecmoi.put("idmenu",idmenu);
        tiecmoi.put("idservice",idsv);
        tiecmoi.put("slk", slk);
        tiecmoi.put("selectedTime", selectedTime);
        tiecmoi.put("timestamp", new Date());
        fb.collection("hopdong").add(tiecmoi).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("DattiecActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                Toast.makeText(AddContactActivity.this, "Đặt tiệc thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddContactActivity.this, HopdongActivity.class);
                intent.putExtra("hopdongId", documentReference.getId());
                intent.putExtra("Userid", userID);
                intent.putExtra("Tenkh", Tenkh);
                intent.putExtra("sdt", sdt);
                intent.putExtra("loaisanh", loaisanh);
                intent.putExtra("date", datebook);
                intent.putExtra("idmenu", idmenu);
                intent.putExtra("idservice", idsv);
                intent.putExtra("slk", slk);
                intent.putExtra("selectedTime", selectedTime);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DattiecActivity", "Error adding document", e);
                Toast.makeText(AddContactActivity.this, "Đặt tiệc thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_THUC_DON) {
                idmenu1 = data.getStringExtra("idmenu");
            }
            else if (requestCode == REQUEST_CODE_SERVICE) {
                idservice = data.getStringExtra("idservice");
            }
        }
    }
}