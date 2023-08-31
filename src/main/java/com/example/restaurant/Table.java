
package com.example.restaurant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Table {
    private int tid;
    private int eid;
    private int seats;
    private double total;

    private TableState tstate;

    private ObservableList<Dish> orders;

    public Table(int tid, int eid, int seats, double total, String tstate) {
        this.tid = tid;
        this.eid = eid;
        this.seats = seats;
        this.total = total;
        this.tstate = TableState.valueOf(tstate);
        this.orders = FXCollections.observableList(new ArrayList<>());
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public TableState getTstate() {
        return tstate;
    }

    public void setTstate(TableState tstate) {
        this.tstate = tstate;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tid=" + tid +
                ", eid=" + eid +
                ", seats=" + seats +
                ", charge=" + total +
                ", tstate=" + tstate +
                '}';
    }

    public ObservableList<Dish> getOrders() {
        return orders;
    }
}

