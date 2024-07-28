package com.example.doandiamond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.Api.CreateOrder;
import com.example.Model.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;

import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class PayActivity extends AppCompatActivity {
    TextView name,phone,deposit,room,date,slk,time,txt_succes;
    String ten,sdt,sanh,ngay,khach,gio,trangthai,documentId;
    int tiencoc;
    ImageView back,gifImageView;
    AppCompatButton btnpay;    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent i=getIntent();
        documentId=i.getStringExtra("documentId");
        ten=i.getStringExtra("name");
        sdt=i.getStringExtra("phone");
        sanh=i.getStringExtra("cate");
        ngay=i.getStringExtra("date");
        khach=i.getStringExtra("slk");
        gio=i.getStringExtra("time");
        trangthai=i.getStringExtra("status");
        tiencoc=i.getIntExtra("deposit",0);
        init();
        name.setText(ten);
        phone.setText(sdt);
        room.setText(sanh);
        date.setText(ngay);
        slk.setText(khach);
        time.setText(gio);
        deposit.setText(formatPrice(tiencoc));

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder(String.valueOf(tiencoc));
                    String code = data.getString("return_code");
                    if (code.equals("1")) {
                        String token = data.getString("zp_trans_token");
                        ZaloPaySDK.getInstance().payOrder(PayActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(String s, String s1, String s2) {
                                PayDeposit(documentId);
                                txt_succes.setVisibility(View.VISIBLE);
                                gifImageView.setVisibility(View.VISIBLE);
                                btnpay.setVisibility(View.GONE);
                            }

                            @Override
                            public void onPaymentCanceled(String s, String s1) {
                                Toast.makeText(PayActivity.this,"Hủy giao dịch",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                Toast.makeText(PayActivity.this,"Giao dịch lỗi",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        back.setOnClickListener(v -> finish());
    }
    private void init(){
        name=findViewById(R.id.name_pay);
        phone=findViewById(R.id.phone_pay);
        deposit=findViewById(R.id.deposit_pay);
        room=findViewById(R.id.cate_room_pay);
        date=findViewById(R.id.datebook_pay);
        slk=findViewById(R.id.khach_pay);
        time=findViewById(R.id.time_pay);
        btnpay=findViewById(R.id.btnpay_pay);
        txt_succes=findViewById(R.id.txtsucces);
        back=findViewById(R.id.back);
        gifImageView=findViewById(R.id.gifImageView);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
    private void PayDeposit(String documentId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (documentId != null && !documentId.isEmpty()) {
            db.collection("hopdong").whereEqualTo("Idyc", documentId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    db.collection("hopdong").document(document.getId()).update("status", "paid")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(PayActivity.this, "Thanh toán tiền cọc thành công", Toast.LENGTH_SHORT).show();
                                                    db.collection("tiec").document(documentId).update("status","paid");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PayActivity.this, "Thanh toán tiền cọc thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(PayActivity.this, "Không tìm thấy hợp đồng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PayActivity.this, "Lỗi khi truy vấn Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(PayActivity.this, "Document ID không hợp lệ", Toast.LENGTH_SHORT).show();
        }
    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}