package com.example.Quanly;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.DichvuHDAdapter;
import com.example.Adapter.MenuHDAdapter;
import com.example.Model.DichVu;
import com.example.Model.Dish;
import com.example.doandiamond.App;
import com.example.doandiamond.PaymentActivity;
import com.example.doandiamond.R;
import com.example.doandiamond.dndkActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HopdongActivity extends AppCompatActivity {
    NavigationView nav_admin;
    DrawerLayout drawerLayout;
    private FirebaseFirestore fb;
    TextView tonggiatd, tongtiec, tonggiadv, tgiasanh,tiencoc;
    EditText tencodau,tenchure;
    Button tao;
    String idmenu, iddv, tenkh, sdt, ngay, thucdon, gio ;
    String idyc;
    String loaisanh;
    int giasanh=0;
    int slk=0;
    int giatd=0;
    int giadv=0;
    int tonggiatiec;
    int tongtiencoc;
    private TextView contractDetails;
    private RecyclerView recyclerView, recyclerView1;
    private MenuHDAdapter menuHDAdapter;
    private DichvuHDAdapter dichvuHDAdapter;
    private List<Dish> dishList;
    private List<DichVu> dichVuList;
    private static final int PAYPAL_REQUEST_CODE = 7171;
    String documentId;
    App MyPayPalConfig;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hopdong);
        fb = FirebaseFirestore.getInstance();
        tonggiatd=findViewById(R.id.tvtgiatd);
        tonggiadv=findViewById(R.id.tvtgiadv);
        tongtiec=findViewById(R.id.tvtgiatiec);
        tgiasanh=findViewById(R.id.tvtgiasanh);
        tiencoc = findViewById(R.id.tiencoc);
        tencodau = findViewById(R.id.tencodau);
        tenchure = findViewById(R.id.tenchure);
        tao=findViewById(R.id.taohd);
        contractDetails = findViewById(R.id.contractDetails);
        recyclerView=findViewById(R.id.rc_menuhd);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dishList = new ArrayList<>();
        menuHDAdapter = new MenuHDAdapter(dishList,this);
        recyclerView.setAdapter(menuHDAdapter);
        recyclerView1=findViewById(R.id.rc_dichvuhd);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        dichVuList = new ArrayList<>();
        dichvuHDAdapter = new DichvuHDAdapter(dichVuList,this);
        recyclerView1.setAdapter(dichvuHDAdapter);
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        if (documentId != null) {
            loadContractDetails(documentId);
            tao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taohopdong();
                }
            });
        }
        else {
            documentId = intent.getStringExtra("hopdongId");
            idyc = intent.getStringExtra("hopdongId");
            tenkh = intent.getStringExtra("Tenkh");
            sdt = intent.getStringExtra("sdt");
            loaisanh = intent.getStringExtra("loaisanh");
            ngay = intent.getStringExtra("date");
            idmenu = intent.getStringExtra("idmenu");
            iddv = intent.getStringExtra("idservice");
            slk = Integer.parseInt(intent.getStringExtra("slk"));
            gio = intent.getStringExtra("selectedTime");
            loadContractDetails();
            tao.setOnClickListener(v -> taohopdongadmin(documentId));
        }


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
                    startActivity(new Intent(HopdongActivity.this, AdminConfirmActivity.class));
                } else if (id == R.id.mn_logout) {
                    startActivity(new Intent(HopdongActivity.this, dndkActivity.class));
                } else if (id == R.id.mn_taohd) {
                    startActivity(new Intent(HopdongActivity.this, HopdongActivity.class));
                }else if(id == R.id.trangthai){
                    startActivity(new Intent(HopdongActivity.this,TrangthaiSanhActivity.class));
                } else {
                    return false;
                }
                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });
    }

    private void taohopdong() {
            Map<String, Object> hopdongmoi = new HashMap<>();
            hopdongmoi.put("tencodau",tencodau.getText().toString());
            hopdongmoi.put("tenchure",tenchure.getText().toString());
            hopdongmoi.put("Idyc", idyc);
            hopdongmoi.put("Tenkh", tenkh);
            hopdongmoi.put("sdt", sdt);
            hopdongmoi.put("loaisanh", loaisanh);
            hopdongmoi.put("date", ngay);
            hopdongmoi.put("thucdon", thucdon);
            hopdongmoi.put("idmenu",idmenu);
            hopdongmoi.put("idservice",iddv);
            hopdongmoi.put("slk", slk);
            hopdongmoi.put("soban",Math.ceil(slk / 10.0) );
            hopdongmoi.put("selectedTime", gio);
            hopdongmoi.put("giasanh", giasanh);
            hopdongmoi.put("giathucdon", giatd);
            hopdongmoi.put("giadichvu", giadv);
            hopdongmoi.put("tiencoc",tongtiencoc);
            hopdongmoi.put("tongtien", tonggiatiec);
            hopdongmoi.put("timestamp", new Date());
            fb.collection("hopdong").add(hopdongmoi).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("tiec").document(documentId).update("status", "created");
                    db.collection("tiec").document(documentId).update("tiencoc", tongtiencoc);
                    Log.d("HopdongActivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(HopdongActivity.this, "Tạo hợp đồng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("HopdongActivity", "Error adding document", e);
                    Toast.makeText(HopdongActivity.this, "Tạo hợp đồng thất bại", Toast.LENGTH_SHORT).show();
                }
            });

        }
    private void taohopdongadmin(String hopdongId) {
        Map<String, Object> hopdongmoi = new HashMap<>();
        hopdongmoi.put("tencodau", tencodau.getText().toString());
        hopdongmoi.put("tenchure", tenchure.getText().toString());
        hopdongmoi.put("Idyc", idyc);
        hopdongmoi.put("Tenkh", tenkh);
        hopdongmoi.put("sdt", sdt);
        hopdongmoi.put("loaisanh", loaisanh);
        hopdongmoi.put("date", ngay);
        hopdongmoi.put("thucdon", thucdon);
        hopdongmoi.put("idmenu", idmenu);
        hopdongmoi.put("idservice", iddv);
        hopdongmoi.put("slk", slk);
        hopdongmoi.put("soban", Math.ceil(slk / 10.0));
        hopdongmoi.put("selectedTime", gio);
        hopdongmoi.put("giasanh", giasanh);
        hopdongmoi.put("giathucdon", giatd);
        hopdongmoi.put("giadichvu", giadv);
        hopdongmoi.put("tiencoc", tongtiencoc);
        hopdongmoi.put("tongtien", tonggiatiec);
        hopdongmoi.put("timestamp", new Date());
        fb.collection("hopdong").document(hopdongId).set(hopdongmoi) // Cập nhật tài liệu đã tồn tại
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("hopdong").document(hopdongId).update("status", "created");
                        Log.d("HopdongActivity", "DocumentSnapshot updated with ID: " + hopdongId);
                        Toast.makeText(HopdongActivity.this, "Tạo hợp đồng thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("HopdongActivity", "Error updating document", e);
                        Toast.makeText(HopdongActivity.this, "Tạo hợp đồng thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void loadMenu() {
        fb.collection("CONFIRMED_MENU")
                .document(idmenu) // Replace with your document ID
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Map<String, Object>> dishes = (List<Map<String, Object>>) documentSnapshot.get("dishes");
                            for (Map<String, Object> dishMap : dishes) {
                                String cate = (String) dishMap.get("cate");
                                String image = (String) dishMap.get("image");
                                String name = (String) dishMap.get("name");
                                int price = ((Long) dishMap.get("price")).intValue();
                                Dish dish = new Dish(name,image,cate,price);
                                dishList.add(dish);
                            }
                            menuHDAdapter.notifyDataSetChanged();
                            giatd = menuHDAdapter.getTotalPrice();
                            tonggiatd.setText(giatd + " VND/bàn");
                            calculateTotalPrice();
                            tongtiencocc();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HopdongActivity.this, "Lỗi khi tải menu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadDichvu() {
        fb.collection("Confirmed_Service")
                .document(iddv) // Replace with your document ID
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Map<String, Object>> services = (List<Map<String, Object>>) documentSnapshot.get("services");
                            if(services!=null) {
                                for (Map<String, Object> dishMap : services) {
                                    String image = (String) dishMap.get("image");
                                    String name = (String) dishMap.get("name");
                                    int price = ((Long) dishMap.get("price")).intValue();
                                    DichVu dichVu = new DichVu(image, name, price);
                                    dichVuList.add(dichVu);
                                }
                                dichvuHDAdapter.notifyDataSetChanged();
                                giadv = dichvuHDAdapter.getTotalPrice1();
                                tonggiadv.setText(giadv + " VND");
                                calculateTotalPrice();
                                tongtiencocc();
                            }
                            else {
                                giadv = 0;
                                tonggiadv.setText(giadv + " VND");
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HopdongActivity.this, "Lỗi khi tải menu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loadGiasanh() {
        fb.collection("Room").whereEqualTo("ten", loaisanh)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                            giasanh= documentSnapshot.getLong("gia").intValue();
                            tgiasanh.setText(giasanh+ " VND/bàn");
                        }
                        calculateTotalPrice();
                        tongtiencocc();
                    }
                });
    }
    // Nếu tạo mới yêu cầu đặt tiệc của admin
    private void loadContractDetails() {
        StringBuilder contractText = new StringBuilder();
        contractText.append("Tên khách hàng: ").append(tenkh).append("\n");
        contractText.append("Số điện thoại: ").append(sdt).append("\n");
        contractText.append("Ngày: ").append(ngay).append("\n");
        contractText.append("Sảnh: ").append(loaisanh).append("\n");
        contractText.append("Số lượng khách: ").append(slk).append("\n");
        contractText.append("Giờ cưới: ").append(gio).append("\n");
        contractDetails.setText(contractText.toString());

        if (idmenu != null) {
            loadMenu();
        } else {
            Toast.makeText(HopdongActivity.this, "Menu rỗng", Toast.LENGTH_SHORT).show();
        }
        if (iddv != null) {
            loadDichvu();
        } else {
            giadv = 0;
            tonggiadv.setText(giadv + " VND");
            Toast.makeText(HopdongActivity.this, "Dịch vụ rỗng", Toast.LENGTH_SHORT).show();
        }
        if (loaisanh != null) {
            loadGiasanh();
        } else {
            Toast.makeText(HopdongActivity.this, "Sảnh rỗng", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadContractDetails(String documentId) {
        DocumentReference docRef = fb.collection("tiec").document(documentId);
        idyc=documentId;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        StringBuilder contractText = new StringBuilder();
                        contractText.append("Tên khách hàng: ").append(document.getString("Tenkh")).append("\n");
                        contractText.append("Số điện thoại: ").append(document.getString("sdt")).append("\n");
                        contractText.append("Ngày: ").append(document.getString("date")).append("\n");
                        contractText.append("Sảnh: ").append(document.getString("loaisanh")).append("\n");
                        contractText.append("Số lượng khách: ").append(document.getString("slk")).append("\n");
                        contractText.append("Giờ cưới: ").append(document.getString("selectedTime")).append("\n");
                        tenkh=document.getString("Tenkh");
                        sdt=document.getString("sdt");
                        ngay=document.getString("date");
                        thucdon=document.getString("thucdon");
                        gio=document.getString("selectedTime");
                        idmenu=document.getString("idmenu");
                        iddv=document.getString("idservice");
                        loaisanh=document.getString("loaisanh");
                        slk= Integer.parseInt(document.getString("slk"));
                        contractDetails.setText(contractText.toString());
                        if (idmenu != null) {
                            loadMenu();
                        } else {
                            Toast.makeText(HopdongActivity.this, "Menu rỗng", Toast.LENGTH_SHORT).show();
                        }
                        if (iddv != null) {
                            loadDichvu();
                        } else {
                            giadv = 0;
                            tonggiadv.setText(giadv + " VND");
                            Toast.makeText(HopdongActivity.this, "Dịch vụ rỗng", Toast.LENGTH_SHORT).show();
                        }
                        if (loaisanh != null) {
                            loadGiasanh();
                        } else {
                            Toast.makeText(HopdongActivity.this, "Sảnh rỗng", Toast.LENGTH_SHORT).show();
                        }
                        //tonggiatiec=(int) Math.ceil(slk/10)*(giasanh+giatd)+giadv;
                        //tongtiec.setText(tonggiatiec+" VND");
                    } else {
                        Log.d("ContractActivity", "No such document");
                        Toast.makeText(HopdongActivity.this, "Không tìm thấy hợp đồng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("ContractActivity", "get failed with ", task.getException());
                    Toast.makeText(HopdongActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void calculateTotalPrice() {
        if (giasanh != 0 && giatd != 0 && slk != 0 ) {
            tonggiatiec = (int) Math.ceil(slk / 10.0) * (giasanh + giatd)+ giadv;
            tongtiec.setText(formatPrice(tonggiatiec));
        }
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
    private void tongtiencocc(){
            tongtiencoc = (int) (tonggiatiec * 0.1);
            tiencoc.setText(formatPrice(tongtiencoc));

    }
}
