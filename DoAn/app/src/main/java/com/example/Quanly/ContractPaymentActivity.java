package com.example.Quanly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.ContractPayment;
import com.example.doandiamond.R;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class ContractPaymentActivity extends AppCompatActivity {
TextView ten,sdt,sanh,ngay,slb,gio,giatd,giadv,giasanh,tongtien,tong;
EditText ban,giops,khac;
int gia_sanh,gia_td,tongchiphi,gia_dv,total,newTotal;
AppCompatButton btn_xnpay;
ImageView backcp;
String documentId,Idyc;
int giagiophucvu=300; //giá mỗi giờ phục vụ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_payment);

        init();

        Intent i=getIntent();
        String name=i.getStringExtra("name");
        String phone=i.getStringExtra("phone");
        String room=i.getStringExtra("loaisanh");
        String date=i.getStringExtra("ngay");
        int mount=i.getIntExtra("slb",0);
        String time=i.getStringExtra("time");
        gia_sanh=i.getIntExtra("giasanh",0);
        gia_td=i.getIntExtra("giatd",0);
        gia_dv=i.getIntExtra("giadv",0);
        total=i.getIntExtra("tongtien",0);
        ten.setText(name);
        sdt.setText(phone);
        sanh.setText(room);
        ngay.setText(date);
        slb.setText(String.valueOf(mount));
        gio.setText(time);
        giatd.setText(formatPrice(gia_td));
        giasanh.setText(formatPrice(gia_sanh));
        giadv.setText(formatPrice(gia_dv));
        tongtien.setText(formatPrice(total));
        documentId=i.getStringExtra("documentId");
        Idyc=i.getStringExtra("Idyc");
        ban.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalAmount();
            }
        });
        giops.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalAmount();
            }
        });
        khac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalAmount();
            }
        });
        btn_xnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddContractPayment(documentId,Idyc);
            }
        });
        backcp.setOnClickListener(v-> finish());
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
    private void init(){
        ten=findViewById(R.id.tenkhcp);
        sdt=findViewById(R.id.sdtcp);
        sanh=findViewById(R.id.loaisanhcp);
        ngay=findViewById(R.id.datecp);
        slb=findViewById(R.id.slbcp);
        gio=findViewById(R.id.selectedTimecp);
        giatd=findViewById(R.id.giatdcp);
        giadv=findViewById(R.id.giaDVcp);
        giasanh=findViewById(R.id.giasanhcp);
        tongtien=findViewById(R.id.tongtiencp);
        tong=findViewById(R.id.txt_total);
        ban=findViewById(R.id.edtslb);
        giops=findViewById(R.id.edtpvps);
        khac=findViewById(R.id.edtcpk);
        backcp=findViewById(R.id.backcp);
        btn_xnpay=findViewById(R.id.btn_xnpay);
    }
    private void updateTotalAmount() {
        int soLuongBan = 0;
        int soGioPhucVu = 0;
        int giachiphikhac=0;

        if (!ban.getText().toString().trim().isEmpty()) {
            soLuongBan = Integer.parseInt(ban.getText().toString().trim());
        }
        if (!giops.getText().toString().trim().isEmpty()) {
            soGioPhucVu = Integer.parseInt(giops.getText().toString().trim());
        }
        if(!khac.getText().toString().trim().isEmpty()){
            giachiphikhac= Integer.parseInt(khac.getText().toString().trim());
        }

        newTotal = tongchiphi + (gia_sanh * soLuongBan) + (gia_td * soLuongBan) + (giagiophucvu * soGioPhucVu) +giachiphikhac;
        tong.setText(formatPrice(newTotal));
    }
    private void AddContractPayment(String documentId,String Idyc) {
        // Lấy ra instance của Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lấy các thông tin từ các TextView và EditText
        String name = ten.getText().toString();
        String phone = sdt.getText().toString();
        String room = sanh.getText().toString();
        String date = ngay.getText().toString();
        String time = gio.getText().toString();
        String quantity = slb.getText().toString();
        int menuPrice = gia_td;
        int servicePrice = gia_dv;
        int lobbyPrice = gia_sanh;
        int totalBooking = total;
        int totalContract = newTotal;
        int total = totalBooking + totalContract;

        // Tạo một đối tượng ContractPayment
        ContractPayment contractPayment = new ContractPayment(name, phone, room, date, time, quantity, menuPrice,
                servicePrice, lobbyPrice, totalBooking, totalContract, total,documentId,Idyc);

        // Lưu đối tượng này vào Firestore
        db.collection("Contract_Payment")
                .add(contractPayment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ContractPaymentActivity.this,"Thanh toán hợp đồng thành công",Toast.LENGTH_SHORT).show();
                    db.collection("hopdong").document(documentId).update("status","success");
                    db.collection("tiec").document(Idyc).update("status","success");
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi thất bại
                    // Ví dụ: hiển thị thông báo lỗi, ghi log, hoặc làm gì đó khác
                    Toast.makeText(ContractPaymentActivity.this,"Thanh toán hợp đồng thất bại",Toast.LENGTH_SHORT).show();

                });
    }

}