package com.example.restaurantledger;

import java.util.ArrayList;
import java.util.List;

public class Ledger {
    private List<LedgerEntry> entries;

    public Ledger() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(LedgerEntry entry) {
        entries.add(entry);
    }

    public List<LedgerEntry> getEntries() {
        return entries;
    }

    public double calculateTotalBalance() {
        double totalBalance = 0.0;
        for (LedgerEntry entry : entries) {
            totalBalance += entry.getBalance();
        }
        return totalBalance;
    }

    // You can add more methods as needed for ledger management.
}