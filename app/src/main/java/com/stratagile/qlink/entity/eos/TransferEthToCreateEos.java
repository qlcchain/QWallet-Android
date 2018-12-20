package com.stratagile.qlink.entity.eos;

public class TransferEthToCreateEos {
    private String from;

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

    public String getEthValue() {
        return ethValue;
    }

    public void setEthValue(String ethValue) {
        this.ethValue = ethValue;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCostDetail() {
        return costDetail;
    }

    public void setCostDetail(String costDetail) {
        this.costDetail = costDetail;
    }

    private String to;
    private String ethValue;
    private String cost;
    private String costDetail;

}
