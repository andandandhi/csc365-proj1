
package com.example.restaurant;

import javafx.beans.property.SimpleStringProperty;

public class Table {
    private int tid;
    private int eid;
    private int seats;
    private double total;

    private TableState tstate;

    private SimpleStringProperty headerString;

    private SimpleStringProperty serverString;

    private SimpleStringProperty totalString;


    public Table(int tid, int eid, int seats, double total, String tstate) {
        this.tid = tid;
        this.eid = eid;
        this.seats = seats;
        this.total = total;
        this.tstate =  TableState.valueOf(tstate);
        this.headerString = new SimpleStringProperty(
                "Table " + tid + " - "
                + tstate.toString().charAt(0) + tstate.toString().substring(1).toLowerCase()
        );

        this.serverString = new SimpleStringProperty("");

        this.setTotalString(new SimpleStringProperty("Total: " + this.total));
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

    public void changeServer(Employee employee) {
        if(employee == null) {
            this.serverString.set("");
        } else {
            this.serverString.set(employee.getEid() + " - " + employee.getEname());
        }
    }

    public SimpleStringProperty getServerString() {
        return this.serverString;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
        this.getTotalString().set("Total: " + this.getTotal());
    }


    public void setTstate(TableState tstate) {
        this.tstate = tstate;
        this.getHeaderString().set("Table " + tid + " - "
                + tstate.toString().charAt(0) + tstate.toString().substring(1).toLowerCase());
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

    public SimpleStringProperty getHeaderString() {
        return this.headerString;
    }

    public SimpleStringProperty getTotalString() {
        return totalString;
    }

    public void setTotalString(SimpleStringProperty totalString) {
        this.totalString = totalString;
    }
}

