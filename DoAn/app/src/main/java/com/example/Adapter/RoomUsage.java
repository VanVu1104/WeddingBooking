package com.example.Adapter;

public class RoomUsage {
    private String loaisanh;
    private int solan;


    public RoomUsage(String loaisanh, int solan) {
        this.loaisanh = loaisanh;
        this.solan = solan;
    }

    public String getLoaisanh() {
        return loaisanh;
    }

    public void setLoaisanh(String loaisanh) {
        this.loaisanh = loaisanh;
    }

    public int getSolan() {
        return solan;
    }

    public void setSolan(int solan) {
        this.solan = solan;
    }
}
