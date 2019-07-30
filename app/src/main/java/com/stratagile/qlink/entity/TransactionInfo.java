package com.stratagile.qlink.entity;

public class TransactionInfo {
    private AllWallet.WalletType transactionType;
    private String transactionToken;
    private String transactionValue;
    private String transationHash;
    private String from;
    private String to;
    private long timestamp;
    private String transactionState;
    private int tokenDecimals;
    private String showAddress;

    public String getShowAddress() {
        return showAddress;
    }

    public void setShowAddress(String showAddress) {
        this.showAddress = showAddress;
    }

    public int getTokenDecimals() {
        return tokenDecimals;
    }

    public void setTokenDecimals(int tokenDecimals) {
        this.tokenDecimals = tokenDecimals;
    }

    public String getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(String transactionState) {
        this.transactionState = transactionState;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    private String owner;

    public String getTransationHash() {
        return transationHash;
    }

    public void setTransationHash(String transationHash) {
        this.transationHash = transationHash;
    }

    public AllWallet.WalletType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(AllWallet.WalletType transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(String transactionToken) {
        this.transactionToken = transactionToken;
    }

    public String getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(String transactionValue) {
        this.transactionValue = transactionValue;
    }
}