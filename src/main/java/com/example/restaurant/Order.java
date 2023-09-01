package com.example.restaurant;

public class Order {
    private int tid;
    private int did;
    private int oid;

    private Dish dish;

    public Order(int oid, int tid, int did, Dish dish) {
        this.oid = oid;
        this.tid = tid;
        this.did = did;
        this.dish = dish;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
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

    public Dish getDish() {
        return this.dish;
    }
}