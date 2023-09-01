package com.example.restaurantledger;

import javafx.beans.property.*;

import java.sql.Date;


public class LedgerEntry {
    private final IntegerProperty lid;
    private final ObjectProperty<Date> date;
    private final StringProperty note;
    private final DoubleProperty balance;

    public LedgerEntry(int lid, Date date, String note, double balance) {
        this.lid = new SimpleIntegerProperty(lid);
        this.date = new SimpleObjectProperty<>(date);
        this.note = new SimpleStringProperty(note);
        this.balance = new SimpleDoubleProperty(balance);
    }

    public int getLid() {
        return lid.get();
    }

    public IntegerProperty lidProperty() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid.set(lid);
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public double getBalance() {
        return balance.get();
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }
}
