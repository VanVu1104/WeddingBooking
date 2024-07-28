package com.example.Model;

public class Dish {
    private String name, image,cate,user,mota,id;
    private int price;

    public Dish() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dish(String name, String image, String cate, int price) {
        this.name = name;
        this.image = image;
        this.cate = cate;
        this.price = price;
    }

    public Dish(String cate, String image, String mota, String name, int price) {
        this.cate = cate;
        this.image = image;
        this.mota = mota;
        this.name = name;
        this.price = price;
    }

    public Dish(String name, String image, String cate, int price, String user, String mota) {
        this.name = name;
        this.mota=mota;
        this.image = image;
        this.user=user;
        this.cate = cate;
        this.price=price;
    }


    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPrice() {
        return price;
    }


    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return cate;
    }

    public void setCategory(String cate) {
        this.cate = cate;
    }
}
