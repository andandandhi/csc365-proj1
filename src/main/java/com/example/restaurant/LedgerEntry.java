
package com.example.restaurant;

import java.sql.Date;

public class LedgerEntry {
    private int lid;
    private Date date;
    private String note;

    private double balance;

    public LedgerEntry(int lid, Date date, String note, double balance) {
        this.lid = lid;
        this.date = date;
        this.note = note;
        this.balance = balance;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public Date getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public double getBalance() {
        return balance;
    }

}
