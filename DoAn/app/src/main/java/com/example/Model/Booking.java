package com.example.Model;

public class Booking {
    private String name,phone,cate_room,date,selectedTime,status,documentId,slk;
    private int deposit;
    public Booking(){}

    public Booking(String name, String phone, String cate_room, String date, String selectedTime, String status, String slk, int deposit,String documentId) {
        this.name = name;
        this.phone = phone;
        this.cate_room = cate_room;
        this.date = date;
        this.selectedTime = selectedTime;
        this.status = status;
        this.slk = slk;
        this.deposit = deposit;
        this.documentId=documentId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public String getCate_room() {
        return cate_room;
    }

    public void setCate_room(String cate_room) {
        this.cate_room = cate_room;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSlk() {
        return slk;
    }

    public void setSlk(String slk) {
        this.slk = slk;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }
}
