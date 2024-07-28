package com.example.doandiamond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Adapter.FavoriteListAdapter;
import com.example.Model.Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteListActivity extends AppCompatActivity implements FavoriteListAdapter.ProductCallback2 {
    RecyclerView rvFav;
    FirebaseFirestore db;
    FirebaseUser auth;
    String user;
    List<Dish> dishList;
    FavoriteListAdapter listAdapter;
    ImageView imv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        init();
    }

    private void LoadFav() {
        db.collection("FAVOURITES").whereEqualTo("user", user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dishList.add(dish);
                            }
                            listAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(FavoriteListActivity.this, "Lỗi khi tải danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void init() {
        rvFav = findViewById(R.id.rvFav);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance().getCurrentUser();

        if (auth != null) {
            user = auth.getUid();
            LoadFav();
        } else {
            Toast.makeText(FavoriteListActivity.this, "Vui lòng đăng nhập để xem danh sách", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean b = getIntent().getBooleanExtra("show menu", false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FavoriteListActivity.this, 2);
        dishList = new ArrayList<>();
        listAdapter = new FavoriteListAdapter(dishList, FavoriteListActivity.this, this, b);
        rvFav.setLayoutManager(gridLayoutManager);
        rvFav.setAdapter(listAdapter);
        imv=findViewById(R.id.imvFav);
        imv.setOnClickListener(v-> finish());
    }

    @Override
    public void onItemClick2(String ten, int price, String mota, String anh, String cate, boolean b) {
        Intent i = new Intent(this, DetailDishActivity.class);
        i.putExtra("ten", ten);
        i.putExtra("price", price);
        i.putExtra("cate", cate);
        i.putExtra("mota", mota);
        i.putExtra("anh", anh);
        i.putExtra("show menu", b);
        startActivity(i);
    }
}
