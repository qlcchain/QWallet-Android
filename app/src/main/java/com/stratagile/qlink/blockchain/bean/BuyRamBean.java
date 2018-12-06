package com.stratagile.qlink.blockchain.bean;

public class BuyRamBean {
    private String payer;
    private String receiver;
    private Long bytes;

    public BuyRamBean(String payer, String receiver, Long bytes) {
        this.payer = payer;
        this.receiver = receiver;
        this.bytes = bytes;
    }

    public String getPayer() {

        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Long getBytes() {
        return bytes;
    }

    public void setBytes(Long bytes) {
        this.bytes = bytes;
    }
}
