package com.example.doandiamond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.MenuConfirmAdapter;
import com.example.Model.Dish;
import com.example.Quanly.HopdongActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuConfirmActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MenuConfirmAdapter menuConfirmAdapter;
    ImageView btnBack;
    private List<Dish> dishList = new ArrayList<>();
    private CollectionReference menuList = FirebaseFirestore.getInstance().collection("MENU");
    private CollectionReference confirmedMenuList = FirebaseFirestore.getInstance().collection("CONFIRMED_MENU");
    Button btnLuu, btnThem;
    private String userID;
    private BroadcastReceiver cartItemDeletedReceiver;
    ImageView imvCart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_menu);

        imvCart=findViewById(R.id.imvCart);
        recyclerView = findViewById(R.id.recyclerViewMenu);
        btnLuu = findViewById(R.id.btnLuu);
        btnThem = findViewById(R.id.btnThem);
        btnBack=findViewById(R.id.btnmenuBack);
        Intent i=getIntent();
        boolean b=i.getBooleanExtra("show",false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuConfirmAdapter = new MenuConfirmAdapter(dishList, this);
        recyclerView.setAdapter(menuConfirmAdapter);
        cartItemDeletedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Cập nhật trạng thái của giỏ hàng
                updateEmptyCartMessage();
            }
        };
        userID = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (userID == null) {
            userID = getIntent().getStringExtra("admin_id");
        }
        loadMenuFromFirestore();

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MenuConfirmActivity.this,ThucDonActivity.class);
                i.putExtra("admin_id",userID);
                i.putExtra("show",b);
                startActivity(i);
            }
        });
        btnBack.setOnClickListener(v -> finish());
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSaveMenu();
            }
        });
    }
    protected void onResume() {
        super.onResume();
        loadMenuFromFirestore();
        // Đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).registerReceiver(cartItemDeletedReceiver, new IntentFilter("CartItemDeleted"));
        // Cập nhật thông báo giỏ hàng trống
        updateEmptyCartMessage();
    }
    @Override
    protected void onPause() {super.onPause();
        // Hủy đăng ký BroadcastReceiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartItemDeletedReceiver);
    }

    private void loadMenuFromFirestore() {
        menuList.whereEqualTo("userID",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    dishList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Dish dish = document.toObject(Dish.class);
                        dish.setId(document.getId());
                        dish.setCategory(document.getString("cate"));
                        dishList.add(dish);
                    }
                    menuConfirmAdapter.notifyDataSetChanged();
                    updateEmptyCartMessage();
                } else {
                    Toast.makeText(MenuConfirmActivity.this, "Lỗi khi tải menu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAndSaveMenu() {
        int mona1Count = 0;
        int mona2Count = 0;
        int mona3Count = 0;

        for (Dish dish : dishList) {
            if ("mona1".equals(dish.getCategory())||"monau1".equals(dish.getCategory())) {
                mona1Count++;
            } else if ("mona2".equals(dish.getCategory())||"monau2".equals(dish.getCategory())) {
                mona2Count++;
            } else if ("mona3".equals(dish.getCategory())||"monau3".equals(dish.getCategory())) {
                mona3Count++;
            }
        }
        if (mona1Count < 1 || mona1Count > 2) {
            Toast.makeText(this, "Món khai vị từ 1 tới 2 món", Toast.LENGTH_SHORT).show();
        } else if (mona2Count < 3 || mona2Count > 4) {
            Toast.makeText(this, "Món chính từ 1 tới 4 món", Toast.LENGTH_SHORT).show();
        } else if (mona3Count < 1 || mona3Count > 2) {
            Toast.makeText(this, "Món tráng miệng từ 1 tới 2 món", Toast.LENGTH_SHORT).show();
        } else {
            saveMenuToFirestore();
        }
    }

    private void saveMenuToFirestore() {
        Map<String, Object> confirmedMenu = new HashMap<>();
        List<Map<String, Object>> dishes = new ArrayList<>();

        for (Dish dish : dishList) {
            Map<String, Object> dishData = new HashMap<>();
            dishData.put("name", dish.getName());
            dishData.put("price", dish.getPrice());
            dishData.put("image", dish.getImage());
            dishData.put("cate", dish.getCategory());
            dishData.put("userID", userID);
            dishes.add(dishData);
        }
        confirmedMenu.put("dishes", dishes);

        confirmedMenuList.add(confirmedMenu)
                .addOnSuccessListener(documentReference -> {
//                    Intent i=new Intent();
//                    i.putExtra("idmenu",documentReference.getId());
//                    finish();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("idmenu", documentReference.getId()); // Thay thế "your_menu_id" bằng ID thực tế
                    setResult(RESULT_OK, resultIntent);
                    clearMenu();
                    finish();
                    Toast.makeText(MenuConfirmActivity.this, "Thực đơn đã được lưu", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(MenuConfirmActivity.this, "Lỗi khi lưu thực đơn", Toast.LENGTH_SHORT).show());
    }

    private void clearMenu() {
        CollectionReference cart = menuList;
        cart.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                    doc.getReference().delete();
                }
            }
        });
    }
    private void updateEmptyCartMessage() {
        if (dishList.isEmpty()) {
            imvCart.setVisibility(View.VISIBLE);
            btnLuu.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        } else {
            imvCart.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            btnLuu.setVisibility(View.VISIBLE);
        }
    }
}
