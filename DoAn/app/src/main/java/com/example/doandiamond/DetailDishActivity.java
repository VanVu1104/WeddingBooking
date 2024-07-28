package com.example.doandiamond;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.Model.Dish;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DetailDishActivity extends AppCompatActivity {
    TextView txtdetailname,txtdetailprice,txtdetail;
    ImageView imgdetail,btndetailback;
    CheckBox checkbox;
    Button add;
    private CollectionReference favouriteList = FirebaseFirestore.getInstance().collection("FAVOURITES");
    private CollectionReference menuList = FirebaseFirestore.getInstance().collection("MENU");
    FirebaseUser auth;
    FirebaseStorage firestore;
    FirebaseFirestore firestores;
    Dish dish=new Dish();
    String user,admin ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dish);

        //Ánh xạ
        auth=FirebaseAuth.getInstance().getCurrentUser();
        firestores = FirebaseFirestore.getInstance();
        firestore = FirebaseStorage.getInstance().getReference().getStorage();
        txtdetailname=findViewById(R.id.txtdetailname);
        txtdetailprice=findViewById(R.id.txtdetailprice);
        txtdetail=findViewById(R.id.txtdetail);
        imgdetail=findViewById(R.id.imgdetail);
        checkbox=findViewById(R.id.imfav);
        btndetailback=findViewById(R.id.btndetailback);
        add=findViewById(R.id.btnAdd);
        boolean b=getIntent().getBooleanExtra("show menu",false);
        admin=getIntent().getStringExtra("admin_id");
        //Back về trang món ăn
        btndetailback.setOnClickListener(v -> finish());

        // load dữ liệu
        String ten=getIntent().getStringExtra("ten");
        txtdetailname.setText(ten + " ");
        dish.setName(ten);

        int gia=getIntent().getIntExtra("price",0);
        String formatprice=formatPrice(gia);
        txtdetailprice.setText(formatprice);
        dish.setPrice(gia);

        String mota=getIntent().getStringExtra("mota");
        txtdetail.setText(mota);
        dish.setMota(mota);

        String cate=getIntent().getStringExtra("cate");
        dish.setCategory(cate);

        String anh=getIntent().getStringExtra("anh");
        Glide.with(this).load(anh).into(imgdetail);
        dish.setImage(anh);
        if(b)
        {
            add.setVisibility(View.VISIBLE);
        }
        if(auth==null) {
            user=admin;
            checkbox.setBackgroundResource(R.drawable.icon_fav2);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Toast.makeText(DetailDishActivity.this,"Vui lòng đăng nhập để yêu thích món ăn",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            user=auth.getUid();
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Toast.makeText(DetailDishActivity.this, "Thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        checkbox.setBackgroundResource(R.drawable.icon_fav);
                        addFavourite(ten, anh, cate, gia, user, mota);
                        //
                    } else {
                        Toast.makeText(DetailDishActivity.this, "Xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        checkbox.setBackgroundResource(R.drawable.icon_fav2);
                        removeFavourite(anh);
                        //
                    }
                }
            });
        }
        // Check PRODUCTS có trùng với FAVOURITES bằng cách so sánh Image //
        // Nếu có dữ liệu trong đây thì tiến hành checkbox                //
        // Không có thì tiếp tục xuống dưới                               //
        if(user!=null)
        {
            favouriteList.whereEqualTo("image",anh)
                    .whereEqualTo("user",user)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                checkbox.setBackgroundResource(R.drawable.icon_fav);
                                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                        if(b) {
                                            Toast.makeText(DetailDishActivity.this,"Xóa khỏi danh sách yêu thích",Toast.LENGTH_SHORT).show();
                                            checkbox.setBackgroundResource(R.drawable.icon_fav2);
                                            removeFavourite(anh);
                                        } else {
                                            Toast.makeText(DetailDishActivity.this,"Thêm vào danh sách yêu thích",Toast.LENGTH_SHORT).show();
                                            checkbox.setBackgroundResource(R.drawable.icon_fav);
                                            addFavourite(user,ten,cate,gia,user,mota);
                                        }
                                    }
                                });
                            }

                        }
                    });
        }
        else {
            checkbox.setBackgroundResource(R.drawable.icon_fav2);        }


        add.setOnClickListener(v -> addDishToFirestore());


    }
    private void addDishToFirestore() {

        menuList.whereEqualTo("image", dish.getImage()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            // No matching document found, add new dish
                            Map<String, Object> dishData = new HashMap<>();
                            dishData.put("name", dish.getName());
                            dishData.put("price", dish.getPrice());
                            dishData.put("image", dish.getImage());
                            dishData.put("cate", dish.getCategory());
                            dishData.put("userID", user);
                            menuList.add(dishData)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(DetailDishActivity.this, "Món ăn đã được thêm vào thực đơn", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(DetailDishActivity.this, "Lỗi khi thêm món ăn", Toast.LENGTH_SHORT).show()
                                    );
                        } else {
                            // Matching document found, dish already exists
                            Toast.makeText(DetailDishActivity.this, "Món ăn đã tồn tại trong thực đơn", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Query failed
                        Toast.makeText(DetailDishActivity.this, "Lỗi khi kiểm tra món ăn", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    public void addFavourite(String name,String anh,String category,int gia, String user,String mota) {
        dish = new Dish(name,anh,category,gia,user,mota);
        favouriteList.add(dish);
    }
    public void removeFavourite(String anh) {
        favouriteList.whereEqualTo("image",anh)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String documentId = "";

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentId = documentSnapshot.getId();
                        }
                        favouriteList.document(documentId).delete();
                    }
                });

    }
    private String formatPrice(int price) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### VNĐ");
        return decimalFormat.format(price);
    }
}