package com.example.restaurant;

public class Order {
    private int tid;

    private int did;

    public Order(int tid, int did) {
        this.tid = tid;
        this.did = did;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }
}
