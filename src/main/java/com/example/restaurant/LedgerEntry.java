package com.example.restaurant;

public class LedgerEntry {
    private int lid;
    private String date; //TODO use Date instead of String
    private String note;

    public LedgerEntry(int lid, String date, String note, double balance) {
        this.lid = lid;
        this.date = date;
        this.note = note;
        this.balance = balance;
    }

    private double balance;


    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
