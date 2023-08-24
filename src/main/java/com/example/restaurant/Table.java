package com.example.restaurant;

public class Table {
    private int tid;
    private int sid;
    private int seats;
    private double charge;

    private boolean vacant;

    public Table(int tid, int sid, int seats, double charge, boolean vacant) {
        this.tid = tid;
        this.sid = sid;
        this.seats = seats;
        this.charge = charge;
        this.vacant = vacant;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public boolean isVacant() {
        return vacant;
    }

    public void setVacant(boolean vacant) {
        this.vacant = vacant;
    }
}
