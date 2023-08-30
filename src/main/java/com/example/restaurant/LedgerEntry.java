
package com.example.restaurant;

import java.sql.Date;

public class LedgerEntry {
    private int lid;
    private Date date; //TODO use Date instead of String
    private String note;

    private double balance;

    public LedgerEntry(int lid, Date date, String note, double balance) {
        this.lid = lid;
        this.date = date;
        this.note = note;
        this.balance = balance;
    }


    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
