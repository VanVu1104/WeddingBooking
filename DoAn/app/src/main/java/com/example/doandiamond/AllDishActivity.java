package com.example.doandiamond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Adapter.DishAdapter;
import com.example.Model.Dish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllDishActivity extends AppCompatActivity implements DishAdapter.ProductCallback {
    RecyclerView rv;
    FirebaseFirestore firestore;
    DishAdapter dishAdapter;
    List<Dish> dishList;
    ImageView img;
    TextView txtTilte;
    String admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_dish);

        firestore=FirebaseFirestore.getInstance();
        String cate=getIntent().getStringExtra("loai");
        String cateName=getIntent().getStringExtra("ten");
        admin=getIntent().getStringExtra("admin_id");
        //Ánh xạ
        img=findViewById(R.id.btnBack);
        txtTilte=findViewById(R.id.txtTilte);


        boolean b=getIntent().getBooleanExtra("show menu",false);
        if (cateName != null) {
            txtTilte.setText(cateName);
        }
        rv=findViewById(R.id.rvDish);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(AllDishActivity.this,2);
        dishList=new ArrayList<>();
        dishAdapter=new DishAdapter(dishList,this,this,b,admin);
        rv.setLayoutManager(gridLayoutManager);
        rv.setAdapter(dishAdapter);
        if(cate!=null && cate.equalsIgnoreCase("mona1")) {
            firestore.collection("AllDish").whereEqualTo("cate", "mona1")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else if(cate!=null && cate.equalsIgnoreCase("mona2")) {
            firestore.collection("AllDish").whereEqualTo("cate", "mona2")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else  if(cate!=null && cate.equalsIgnoreCase("mona3")) {
            firestore.collection("AllDish").whereEqualTo("cate", "mona3")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

        else  if(cate!=null && cate.equalsIgnoreCase("monau1")) {
            firestore.collection("AllDish").whereEqualTo("cate", "monau1")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else  if(cate!=null && cate.equalsIgnoreCase("monau2")) {
            firestore.collection("AllDish").whereEqualTo("cate", "monau2")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else if(cate!=null && cate.equalsIgnoreCase("monau3")) {
            firestore.collection("AllDish").whereEqualTo("cate", "monau3")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        else {
            firestore.collection("AllDish")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                Dish dish = documentSnapshot.toObject(Dish.class);
                                dish.setCategory(cate);
                                dishList.add(dish);
                                dishAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onItemClick(String ten, int price, String mota, String anh,String cate,boolean b,String admin) {
        Intent i=new Intent(this, DetailDishActivity.class);
        i.putExtra("ten",ten);
        i.putExtra("price",price);
        i.putExtra("cate",cate);
        i.putExtra("mota",mota);
        i.putExtra("anh",anh);
        i.putExtra("show menu",b);
        i.putExtra("admin_id",admin);
        startActivity(i);
    }
}