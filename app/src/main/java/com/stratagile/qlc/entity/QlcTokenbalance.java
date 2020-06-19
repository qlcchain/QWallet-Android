package com.stratagile.qlc.entity;

public class QlcTokenbalance {


    /**
     * balance : 10000000000
     * pending : 100
     */

    private String symbol;
    private String balance;
    private String vote;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "QlcTokenbalance{" +
                "symbol='" + symbol + '\'' +
                ", balance='" + balance + '\'' +
                ", pending='" + pending + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String pending;
    private String address;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

}
