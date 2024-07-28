package com.example.Quanly;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.Model.BookedDate;
import com.example.Model.event;
import com.example.doandiamond.R;
import com.example.doandiamond.dndkActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrangthaiSanhActivity extends AppCompatActivity {
    NavigationView nav_admin;
    DrawerLayout drawerLayout;

    private MaterialCalendarView calendarView;
    private TextView tvStatus;
    private Spinner spinnerHalls, spinnerTimes;
    private FirebaseFirestore db;
    private List<BookedDate> bookedDates;
    private List<String> hallList;
    private List<String> timeList;

    private String selectedHall;
    private String selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangthai_sanh);

        calendarView = findViewById(R.id.calendarView);
        tvStatus = findViewById(R.id.tvStatus);
        spinnerHalls = findViewById(R.id.spinnerHalls);
        spinnerTimes = findViewById(R.id.spinnerTimes);
        db = FirebaseFirestore.getInstance();
        bookedDates = new ArrayList<>();
        hallList = new ArrayList<>();
        timeList = new ArrayList<>();

        loadHalls();
        loadTimes();

        spinnerHalls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedHall = hallList.get(position);
                loadBookedDates();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = timeList.get(position);
                checkBookingStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = String.format(Locale.getDefault(), "%d/%d/%d", date.getDay(), date.getMonth() + 1, date.getYear());
                checkBookingStatus();
            }
        });
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
                    startActivity(new Intent(TrangthaiSanhActivity.this, AdminConfirmActivity.class));
                } else if (id == R.id.mn_logout) {
                    startActivity(new Intent(TrangthaiSanhActivity.this, dndkActivity.class));
                } else if (id == R.id.mn_taohd) {
                    startActivity(new Intent(TrangthaiSanhActivity.this, ListContractActivity.class));
                }else if(id == R.id.trangthai){
                    startActivity(new Intent(TrangthaiSanhActivity.this,TrangthaiSanhActivity.class));
                } else {
                    return false;
                }
                drawerLayout.closeDrawers();  // Đóng drawer sau khi mục được chọn
                return true;
            }
        });
    }

    private void loadHalls() {
        CollectionReference rooms = db.collection("Room");
        rooms.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    hallList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        hallList.add(document.getString("ten"));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TrangthaiSanhActivity.this, android.R.layout.simple_spinner_item, hallList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerHalls.setAdapter(adapter);
                } else {
                    Toast.makeText(TrangthaiSanhActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadTimes() {
        db.collection("tiec1").document("gio")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            timeList.clear(); // Xóa danh sách trước khi thêm dữ liệu mới

                            if (document.exists()) {
                                String item1 = document.getString("item1");
                                String item2 = document.getString("item2");
                                String item3 = document.getString("item3");
                                if (item1 != null) {
                                    timeList.add(item1);
                                }
                                if (item2 != null) {
                                    timeList.add(item2);
                                }
                                if (item3 != null) {
                                    timeList.add(item3);
                                }
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(TrangthaiSanhActivity.this, android.R.layout.simple_spinner_item, timeList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerTimes.setAdapter(adapter);
                        } else {
                            // Hiển thị thông báo lỗi chi tiết
                            Toast.makeText(TrangthaiSanhActivity.this, "Lỗi khi tải dữ liệu: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void loadBookedDates() {
        CollectionReference contracts = db.collection("hopdong");
        contracts.whereEqualTo("loaisanh", selectedHall).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    bookedDates.clear();
                    clearCalendarDecorators(); // Xóa các ngày đánh dấu trước đó
                    for (DocumentSnapshot document : task.getResult()) {
                        String date = document.getString("date");
                        String timework = document.getString("selectedTime");
                        bookedDates.add(new BookedDate(date, timework));
                    }
                    markBookedDatesOnCalendar();
                } else {
                    Toast.makeText(TrangthaiSanhActivity.this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearCalendarDecorators() {
        calendarView.removeDecorators();
    }

    private void markBookedDatesOnCalendar() {
        List<CalendarDay> dates = new ArrayList<>();
        for (BookedDate bookedDate : bookedDates) {
            String[] parts = bookedDate.getDate().split("/");
            int dayOfMonth = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; // month trong Calendar là từ 0-11
            int year = Integer.parseInt(parts[2]);

            CalendarDay calendarDay = CalendarDay.from(year, month, dayOfMonth);
            dates.add(calendarDay);
        }

        calendarView.addDecorator(new event(Color.RED, dates));
    }

    private void checkBookingStatus() {
        if (selectedDate != null && selectedTime != null) {
            boolean isBooked = false;

            for (BookedDate bookedDate : bookedDates) {
                if (bookedDate.getDate().equals(selectedDate) && bookedDate.getTimework().equals(selectedTime)) {
                    isBooked = true;
                    break;
                }
            }

            if (isBooked) {
                tvStatus.setText("Khoảng thời gian này đã có người đặt");
            } else {
                tvStatus.setText("Khoảng thời gian này chưa có người đặt");
            }
        }
    }
}
