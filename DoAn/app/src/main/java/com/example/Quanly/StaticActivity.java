package com.example.Quanly;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.RoomUsage;
import com.example.Adapter.RoomUsageAdapter;
import com.example.Model.DoanhThu;
import com.example.doandiamond.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticActivity extends AppCompatActivity {
    private FirebaseFirestore mFirestore;

    private RoomUsageAdapter roomUsageAdapter;
    private BarChart barChart;
    private EditText editTextMonth;
    private EditText editTextYear;
    private Button buttonFilter;
    private RecyclerView recyclerView;

    private List<DoanhThu> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static);

        mFirestore = FirebaseFirestore.getInstance();

        barChart = findViewById(R.id.barChart);
        editTextMonth = findViewById(R.id.editTextMonth);
        editTextYear = findViewById(R.id.editTextYear);
        buttonFilter = findViewById(R.id.buttonFilter);
        recyclerView = findViewById(R.id.recyclerView);

        roomUsageAdapter = new RoomUsageAdapter(this, new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(roomUsageAdapter);

        mFirestore.collection("hopdong")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null) {
                        return;
                    }
                    if (snapshot != null && !snapshot.isEmpty()) {
                        mList.clear();  // Clear previous data
                        for (DocumentSnapshot documentSnapshot : snapshot.getDocuments()) {
                            DoanhThu doanhThu = documentSnapshot.toObject(DoanhThu.class);
                            mList.add(doanhThu);
                        }
                        Log.d("AAAA", mList.toString());
                        filterDataAndRefreshChart();  // Refresh chart after data is loaded
                    }
                });

        editTextMonth.setText("06");
        editTextYear.setText("2024");
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDataAndRefreshChart();
            }
        });
    }

    // Method to filter data based on user input and refresh chart

    private void  filterDataAndRefreshChart() {

        String monthText = editTextMonth.getText().toString();
        String yearText = editTextYear.getText().toString();

        if (monthText.isEmpty() || yearText.isEmpty()) {
            Toast.makeText(StaticActivity.this, "Nhập tháng và năm để lọc", Toast.LENGTH_SHORT).show();
            return;
        }

        int month = Integer.parseInt(monthText);
        int year = Integer.parseInt(yearText);

        List<DoanhThu> filteredList = new ArrayList<>();
        for (DoanhThu item : mList) {
            String[] dateParts = item.getDate().split("/");
            if (dateParts.length == 3) {
                int itemMonth = Integer.parseInt(dateParts[1]);
                int itemYear = Integer.parseInt(dateParts[2]);
                if (itemMonth == month && itemYear == year) {
                    filteredList.add(item);
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(StaticActivity.this, "Không có dữ liệu cho tháng " + month + "/" + year, Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data entries for the chart
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < filteredList.size(); i++) {
            try {
                float value = filteredList.get(i).getTongtien() / 1000000f; // Convert to millions for easier visualization
                entries.add(new BarEntry(i, value));
            } catch (NumberFormatException e) {
                Log.e("StaticActivity", "Invalid price format: " + filteredList.get(i).getTongtien());
            }
        }

        if (entries.isEmpty()) {
            Toast.makeText(StaticActivity.this, "Dữ liệu không hợp lệ cho tháng " + month + "/" + year, Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a dataset from the data entries
        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu tháng " + month + "/" + year + " (triệu VND)");

        // Customize dataset
        dataSet.setColor(Color.LTGRAY); // Set color
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueTextSize(10f);

        // Create BarData object from the dataset
        BarData barData = new BarData(dataSet);

        // Set data to the chart
        barChart.setData(barData);

        // Customize chart appearance
        Description description = new Description();
        description.setText("Biểu đồ doanh thu các sảnh ");
        barChart.setDescription(description);
        barChart.invalidate();

        // Calculate room usage count for the selected month and year
        Map<String, Integer> roomUsageMap = new HashMap<>();
        for (DoanhThu item : filteredList) {
            String roomName = item.getLoaisanh().trim();
            int currentCount = roomUsageMap.containsKey(roomName) ? roomUsageMap.get(roomName) : 0;
            roomUsageMap.put(roomName, currentCount + 1);
        }

        // Update RecyclerView with room usage data
        List<RoomUsage> roomUsageList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : roomUsageMap.entrySet()) {
            roomUsageList.add(new RoomUsage(entry.getKey(), entry.getValue()));
        }
        roomUsageAdapter.updateData(roomUsageList);
    }


    public void thydohehe(View view) {
        finish();
    }
}