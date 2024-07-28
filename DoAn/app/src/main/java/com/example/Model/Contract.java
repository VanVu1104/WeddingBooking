package com.example.Model;

public class Contract {
    String name,phone,cate,date,time,status,documentId,Idyc;
    int price_room,price_service,price_menu,total,mount;

    public Contract() {
    }

    public Contract(String name, String phone, String date, String time, int total) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.total = total;
    }

    public Contract(String name, String phone, String cate, String date, String time, int mount, int price_room, int price_service, int price_menu, int total, String status, String documentId, String Idyc) {
        this.name = name;
        this.phone = phone;
        this.cate = cate;
        this.date = date;
        this.time = time;
        this.mount = mount;
        this.price_room = price_room;
        this.price_service = price_service;
        this.price_menu = price_menu;
        this.total = total;
        this.documentId=documentId;
        this.status=status;
        this.Idyc=Idyc;
    }

    public String getIdyc() {
        return Idyc;
    }

    public void setIdyc(String idyc) {
        Idyc = idyc;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }

    public int getPrice_room() {
        return price_room;
    }

    public void setPrice_room(int price_room) {
        this.price_room = price_room;
    }

    public int getPrice_service() {
        return price_service;
    }

    public void setPrice_service(int price_service) {
        this.price_service = price_service;
    }

    public int getPrice_menu() {
        return price_menu;
    }

    public void setPrice_menu(int price_menu) {
        this.price_menu = price_menu;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
