package com.example.Model;

public class DichVu {
    private String  anh1, ten1, mota1,id;
    private int gia1;

    public DichVu() {
    }

    public DichVu(String anh1, String ten1, int gia1) {
        this.anh1 = anh1;
        this.ten1 = ten1;
        this.gia1 = gia1;
    }

    public DichVu(String anh1, String ten1, String mota1, int gia1) {
        this.anh1 = anh1;
        this.ten1 = ten1;
        this.mota1 = mota1;
        this.gia1 = gia1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMota1() {
        return mota1;
    }

    public void setMota1(String mota1) {
        this.mota1 = mota1;
    }

    public int getGia1() {
        return gia1;
    }

    public void setGia1(int gia1) {
        this.gia1 = gia1;
    }

    public String getAnh1() {
        return anh1;
    }

    public void setAnh1(String anh1) {
        this.anh1 = anh1;
    }

    public String getTen1() {
        return ten1;
    }

    public void setTen1(String ten1) {
        this.ten1 = ten1;
    }

}
