package com.stratagile.qlink.entity.stake;

public class StakeType {
    private int type;
    private String stakeQLcAmount;
    private String stakeQlcDays;
    private String neoPriKey;
    private String fromNeoAddress;

    public String getStakeQLcAmount() {
        return stakeQLcAmount;
    }

    public void setStakeQLcAmount(String stakeQLcAmount) {
        this.stakeQLcAmount = stakeQLcAmount;
    }

    public String getStakeQlcDays() {
        return stakeQlcDays;
    }

    public void setStakeQlcDays(String stakeQlcDays) {
        this.stakeQlcDays = stakeQlcDays;
    }

    public String getNeoPriKey() {
        return neoPriKey;
    }

    public void setNeoPriKey(String neoPriKey) {
        this.neoPriKey = neoPriKey;
    }

    public String getFromNeoAddress() {
        return fromNeoAddress;
    }

    public void setFromNeoAddress(String fromNeoAddress) {
        this.fromNeoAddress = fromNeoAddress;
    }

    public String getQlcchainAddress() {
        return qlcchainAddress;
    }

    public void setQlcchainAddress(String qlcchainAddress) {
        this.qlcchainAddress = qlcchainAddress;
    }

    public String getNeoPubKey() {
        return neoPubKey;
    }

    public void setNeoPubKey(String neoPubKey) {
        this.neoPubKey = neoPubKey;
    }

    private String toAddress;

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    private String qlcchainAddress;
    private String neoPubKey;

    public StakeType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
