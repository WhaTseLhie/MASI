package com.example.asus.masi.wallet;

public class Wallet {

    int walletId, custId;
    String date;
    double amount;

    public Wallet(int walletId, int custId, String date, double amount) {
        this.walletId = walletId;
        this.custId = custId;
        this.date = date;
        this.amount = amount;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}