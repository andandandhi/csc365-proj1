package com.example.restaurant;

public class Table {
    private int tid;
    private int sid;
    private int seats;
    private double charge;

    private TableState tstate;

    public Table(int tid, int sid, int seats, double charge, String tstate) {
        this.tid = tid;
        this.sid = sid;
        this.seats = seats;
        this.charge = charge;
        this.tstate = TableState.valueOf(tstate);
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

    public TableState isVacant() {
        return tstate;
    }

    public void setVacant(TableState tstate) {
        this.tstate = tstate;
    }
}
