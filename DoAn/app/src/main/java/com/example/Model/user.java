package com.example.Model;

public class user {
    private  String phone;
    private  String ten;
    private  String userid;

    public user() {
    }


    public user(String phone, String ten, String userid) {
        this.phone = phone;
        this.ten = ten;
        this.userid = userid;

    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }



}
