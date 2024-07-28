package com.example.Model;

public class    DoanhThu {
    private String loaisanh;
    private long tongtien;
    private String date;

    public String getLoaisanh() {
        return loaisanh;
    }

    public void setLoaisanh(String loaisanh) {
        this.loaisanh = loaisanh;
    }

    public long getTongtien() {
        return tongtien;
    }

    public void setTongtien(long tongtien) {
        this.tongtien = tongtien;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DoanhThu{" +
                "loaisanh='" + loaisanh + '\'' +
                ", tongtien='" + tongtien + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
