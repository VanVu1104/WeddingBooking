package com.example.Model;

public class ContractPayment {
    private String name_contract,phone_contract,cate_room_contract,date_contract,selectedTime_contract,slb_contract,Idyc,Idhd;
    private int price_menu_contract,price_service_contract,price_lobby,total_booking,total_contract,total;

    public ContractPayment() {
    }

    public ContractPayment(String name_contract, String phone_contract, String cate_room_contract, String date_contract,
                           String selectedTime_contract, String slb_contract, int price_menu_contract, int price_service_contract,
                           int price_lobby, int total_booking, int total_contract, int total,String Idhd,String Idyc) {
        this.name_contract = name_contract;
        this.phone_contract = phone_contract;
        this.cate_room_contract = cate_room_contract;
        this.date_contract = date_contract;
        this.selectedTime_contract = selectedTime_contract;
        this.slb_contract = slb_contract;
        this.price_menu_contract = price_menu_contract;
        this.price_service_contract = price_service_contract;
        this.price_lobby = price_lobby;
        this.total_booking = total_booking;
        this.total_contract = total_contract;
        this.total = total;
        this.Idhd=Idhd;
        this.Idyc=Idyc;
    }

    public String getIdyc() {
        return Idyc;
    }

    public void setIdyc(String idyc) {
        Idyc = idyc;
    }

    public String getIdhd() {
        return Idhd;
    }

    public void setIdhd(String idhd) {
        Idhd = idhd;
    }

    public String getName_contract() {
        return name_contract;
    }

    public void setName_contract(String name_contract) {
        this.name_contract = name_contract;
    }

    public String getPhone_contract() {
        return phone_contract;
    }

    public void setPhone_contract(String phone_contract) {
        this.phone_contract = phone_contract;
    }

    public String getCate_room_contract() {
        return cate_room_contract;
    }

    public void setCate_room_contract(String cate_room_contract) {
        this.cate_room_contract = cate_room_contract;
    }

    public String getDate_contract() {
        return date_contract;
    }

    public void setDate_contract(String date_contract) {
        this.date_contract = date_contract;
    }

    public String getSelectedTime_contract() {
        return selectedTime_contract;
    }

    public void setSelectedTime_contract(String selectedTime_contract) {
        this.selectedTime_contract = selectedTime_contract;
    }

    public String getSlb_contract() {
        return slb_contract;
    }

    public void setSlb_contract(String slb_contract) {
        this.slb_contract = slb_contract;
    }

    public int getPrice_menu_contract() {
        return price_menu_contract;
    }

    public void setPrice_menu_contract(int price_menu_contract) {
        this.price_menu_contract = price_menu_contract;
    }

    public int getPrice_service_contract() {
        return price_service_contract;
    }

    public void setPrice_service_contract(int price_service_contract) {
        this.price_service_contract = price_service_contract;
    }

    public int getPrice_lobby() {
        return price_lobby;
    }

    public void setPrice_lobby(int price_lobby) {
        this.price_lobby = price_lobby;
    }

    public int getTotal_booking() {
        return total_booking;
    }

    public void setTotal_booking(int total_booking) {
        this.total_booking = total_booking;
    }

    public int getTotal_contract() {
        return total_contract;
    }

    public void setTotal_contract(int total_contract) {
        this.total_contract = total_contract;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
