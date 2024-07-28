package com.example.doandiamond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.R.layout;
import android.annotation.SuppressLint;
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

import com.example.Quanly.HopdongActivity;
import com.example.dichvu.DichVuActivity;
import com.example.dichvu.ServiceConfirmActivity;
import com.example.loginsdtActivity;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DattiecActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_THUC_DON = 1;
    private static final int REQUEST_CODE_SERVICE = 2;
    EditText tenkh,edtgio,sanh,edtslk,edtsdt;
    String edttd;
    TextView edtngay;
    String edtSanh;
    AppCompatButton themtd,themdv;
    Button submit;
    RadioGroup radioGroupp;
    String idmenu1;
    String idservice;
    Spinner spinnerMenu;
    FirebaseAuth auth;
    FirebaseFirestore fb;
    List<String> arrayThucDon;
    ArrayAdapter<String> spinnerAdapter;

    List<String> arraySanh;
    ArrayAdapter<String> spinnerA;
    Spinner spinnerSanh;
    NavigationView nav_mn;
    DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dattiec);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        fb = FirebaseFirestore.getInstance();
        tenkh = findViewById(R.id.tenkhach);
        edtsdt = findViewById(R.id.edtSDT);
        themtd=findViewById(R.id.btnthemtd);
        spinnerSanh= findViewById(R.id.sanh);
        themdv=findViewById(R.id.btnthemdv);

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
                    startActivity(new Intent(DattiecActivity.this, DattiecActivity.class));
                } else if (id == R.id.mn_dichvu) {
                    startActivity(new Intent(DattiecActivity.this, DichVuActivity.class));
                } else if (id == R.id.mn_xacnhan) {
                    startActivity(new Intent(DattiecActivity.this, MainActivity.class));
                } else if (id == R.id.mn_sanh) {
                    startActivity(new Intent(DattiecActivity.this, ListRoomActivity.class));
                } else if (id == R.id.mn_thucdon) {
                    startActivity(new Intent(DattiecActivity.this, ThucDonActivity.class));
                } else if (id == R.id.mn_updatett) {
                    startActivity(new Intent(DattiecActivity.this, CapnhatActivity.class));
                } else if (id == R.id.mn_dn) {
                    startActivity(new Intent(DattiecActivity.this, loginsdtActivity.class));
                }else if (id == R.id.mn_history) {
                    startActivity(new Intent(DattiecActivity.this, BookingHistoryActivity.class));
                } else if (id == R.id.mn_logout) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(DattiecActivity.this, dndkActivity.class));
                } else {
                    return false;
                }

                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });

        //id menu and service
//        idmenu1=getIntent().getStringExtra("idmenu");
//        idservice=getIntent().getStringExtra("idservice");


//        spinnerMenu = (Spinner) findViewById(R.id.thucdon);
        edtngay = findViewById(R.id.ngay);
        final Calendar calendar = Calendar.getInstance();
        final int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        final int thang = calendar.get(Calendar.MONTH);
        final int nam = calendar.get(Calendar.YEAR);
        themtd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(DattiecActivity.this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(DattiecActivity.this, MenuConfirmActivity.class);
                    i.putExtra("show", true);
                    startActivityForResult(i, REQUEST_CODE_THUC_DON);
                }

            }
        });
        themdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(DattiecActivity.this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(DattiecActivity.this, ServiceConfirmActivity.class);
                    i.putExtra("show", true);
                    startActivityForResult(i, REQUEST_CODE_SERVICE);
                }
            }
        });



        edtngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sm = new SimpleDateFormat("mm/dd/yyyy");
                String strDate = sm.format(new Date());
                DatePickerDialog dialog = new DatePickerDialog(DattiecActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int Nam, int Thang, int Ngay) {

                        Thang = Thang + 1;
                        String date = Ngay+"/"+Thang+"/"+Nam;
                        edtngay.setText(date);
                    }
                },nam,thang,ngay);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }});
        edtslk = findViewById(R.id.slk);

        submit = findViewById(R.id.gui);
        radioGroupp = findViewById(R.id.radioGroup);
//        arrayThucDon = new ArrayList<>();
//        loadThucdon();
//        spinnerAdapter = new ArrayAdapter<>(DattiecActivity.this, layout.simple_spinner_item, arrayThucDon);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerMenu.setAdapter(spinnerAdapter);
//        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                edttd =(String) arrayThucDon.get(position);
////
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                edttd = null;
//                Toast.makeText(DattiecActivity.this, "onNothingSelected", Toast.LENGTH_SHORT).show();
//            }
//        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guiyeucau();
            }
        });

        arraySanh = new ArrayList<>();
        loadSanh();
        spinnerA = new ArrayAdapter<>(DattiecActivity.this,layout.simple_spinner_item,arraySanh);
        spinnerA.setDropDownViewResource(layout.simple_spinner_dropdown_item);
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
                Toast.makeText(DattiecActivity.this, "onNothingSelected", Toast.LENGTH_SHORT).show();
            }
        });
        loadGioCuoiFromFirestore();

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
                                Toast.makeText(DattiecActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                            } else {
                                // Cập nhật Adapter khi dữ liệu đã sẵn sàng
                                spinnerA.notifyDataSetChanged();
                            }
                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(DattiecActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void guiyeucau() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the user is logged in
        if (currentUser == null) {
            Toast.makeText(DattiecActivity.this, "Vui lòng đăng nhập để đặt tiệc", Toast.LENGTH_SHORT).show();
            return;
        }

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String Tenkh = tenkh.getText().toString().trim();
            String sdt = edtsdt.getText().toString().trim();
            String loaisanh = edtSanh;
            String idmenu= idmenu1;
            String idsv=idservice;
            String thucdon = edttd;
            String date = edtngay.getText().toString().trim();
            String slk = edtslk.getText().toString().trim();
            int selectedId = radioGroupp.getCheckedRadioButtonId();

            // Kiểm tra các trường không trống
        if (Tenkh.isEmpty()) {
            Toast.makeText(DattiecActivity.this, "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sdt.length() != 10) {
            Toast.makeText(DattiecActivity.this, "Số điện thoại gồm 10 số", Toast.LENGTH_SHORT).show();
            return;
        }
        if (loaisanh == null) {
            Toast.makeText(DattiecActivity.this, "Vui lòng chọn sảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idmenu == null) {
            Toast.makeText(DattiecActivity.this, "Vui lòng chọn thực đơn", Toast.LENGTH_SHORT).show();
            return;
        }
        if (date.isEmpty()) {
            Toast.makeText(DattiecActivity.this, "Vui lòng chọn ngày", Toast.LENGTH_SHORT).show();
            return;
        }
        if (slk.isEmpty()) {
            Toast.makeText(DattiecActivity.this, "Vui lòng nhập số lượng khách", Toast.LENGTH_SHORT).show();
            return;
        }

        int soLuongKhach;
        try {
            soLuongKhach = Integer.parseInt(slk);
        } catch (NumberFormatException e) {
            Toast.makeText(DattiecActivity.this, "Số lượng khách phải là một số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (soLuongKhach > 500) {
            Toast.makeText(DattiecActivity.this, "Số lượng khách không được vượt quá 500", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedId == -1) {
            Toast.makeText(DattiecActivity.this, "Vui lòng chọn giờ cưới", Toast.LENGTH_SHORT).show();
            return;
        }

         fb.collection("hopdong")
                .whereEqualTo("loaisanh", loaisanh)
                .whereEqualTo("date", date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Nếu có bất kỳ tài liệu nào trong kết quả trả về,
                                // nghĩa là đã có tiệc được đặt trong ngày và sảnh này.
                                // Bạn có thể thông báo hoặc xử lý tùy ý ở đây.
                                Toast.makeText(DattiecActivity.this, "Sảnh đã có tiệc vào ngày này", Toast.LENGTH_SHORT).show();
                                return; // Kết thúc vòng lặp khi đã tìm thấy sự kiện
                            }

                            // Nếu không có tiệc nào được đặt, tiến hành thêm bản ghi mới vào Firestore
                            Map<String, Object> tiecmoi = new HashMap<>();
                            tiecmoi.put("Userid", userID);
                            tiecmoi.put("Tenkh", Tenkh);
                            tiecmoi.put("sdt", sdt);
                            tiecmoi.put("loaisanh", loaisanh);
                            tiecmoi.put("date", date);
                            tiecmoi.put("thucdon", thucdon);
                            tiecmoi.put("idmenu", idmenu);
                            tiecmoi.put("idservice", idsv);
                            tiecmoi.put("slk", slk);
                            RadioButton selectedRadioButton = findViewById(selectedId);
                            String selectedTime = selectedRadioButton.getText().toString();
                            tiecmoi.put("selectedTime", selectedTime);
                            tiecmoi.put("status","wait");
                            tiecmoi.put("timestamp", new Date());
                            // Thêm bản ghi mới vào Firestore
                            fb.collection("tiec").add(tiecmoi)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d("DattiecActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(DattiecActivity.this, "Đặt tiệc thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("DattiecActivity", "Error adding document", e);
                                            Toast.makeText(DattiecActivity.this, "Đặt tiệc thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.d("DattiecActivity", "Error getting documents: ", task.getException());
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
    @Override
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