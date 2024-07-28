package com.example.doandiamond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Adapter.BookingHistoryAdapter;
import com.example.Model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;


public class BookingHistoryActivity extends AppCompatActivity implements BookingHistoryAdapter.ProductCallback{
    RecyclerView rvHistory;
    ImageView imvback;
    List<Booking> bookingList;
    BookingHistoryAdapter bookingHistoryAdapter;
    FirebaseUser auth;
    String user;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);



        rvHistory = findViewById(R.id.rvHistory);
        imvback = findViewById(R.id.imvb);
        auth = FirebaseAuth.getInstance().getCurrentUser();

        if (auth != null) {
            user = auth.getUid();
            bookingList = new ArrayList<>();
            bookingHistoryAdapter = new BookingHistoryAdapter(bookingList, this,this);
            rvHistory.setLayoutManager(new LinearLayoutManager(this));
            rvHistory.setAdapter(bookingHistoryAdapter);
            LoadBooking();
        } else {
            Toast.makeText(BookingHistoryActivity.this, "Lịch sử hợp đồng rỗng", Toast.LENGTH_SHORT).show();
        }

        imvback.setOnClickListener(v -> finish());
    }

    private void LoadBooking() {
        db.collection("tiec")
                .whereEqualTo("Userid", user)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookingList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            Booking booking = document.toObject(Booking.class);
                            String name = document.getString("Tenkh");
                            String phone = document.getString("sdt");
                            String cate = document.getString("loaisanh");
                            String date = document.getString("date");
                            String time = document.getString("selectedTime");
                            String mount = document.getString("slk");
                            // Kiểm tra giá trị của "tiencoc" trước khi gọi intValue()
                            Long tiencocValue = document.getLong("tiencoc");
                            int deposit = (tiencocValue != null) ? tiencocValue.intValue() : 0;
                            String status = document.getString("status");

                            booking = new Booking(name, phone, cate, date, time, status, mount, deposit,documentId);
                            bookingList.add(booking);
                        }
                        bookingHistoryAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("BookingHistoryActivity", "Error getting documents: ", task.getException());
                    }
                });
    }


    @Override
    public void onItemClick(String name, String phone, String cate_room, String slk, String selectedTime, String status, int deposit, String documentId,String date) {
        Intent i=new Intent(BookingHistoryActivity.this,PayActivity.class);
        i.putExtra("name",name);
        i.putExtra("phone",phone);
        i.putExtra("cate",cate_room);
        i.putExtra("slk",slk);
        i.putExtra("time",selectedTime);
        i.putExtra("status",status);
        i.putExtra("deposit",deposit);
        i.putExtra("documentId",documentId);
        i.putExtra("date",date);
        startActivity(i);
    }
    protected void onResume(){
        super.onResume();
        LoadBooking();
    }
}
