package com.example.Model;

public class Category {
    private String  name, cate,image;

    public Category() {
    }

    public Category( String name, String image,String cate) {
        this.cate=cate;

        this.name = name;
        this.image = image;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
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
}
