package com.stratagile.qlink.blockchain.bean;

public class BuyRamBean {
    private String payer;
    private String receiver;
    private String quant;

    public BuyRamBean(String payer, String receiver, String quant) {
        this.payer = payer;
        this.receiver = receiver;
        this.quant = quant;
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

    public String getBytes() {
        return quant;
    }

    public void setBytes(String bytes) {
        this.quant = quant;
    }
}
