package com.stratagile.qlink.entity.wifi;

/**
 * Created by huzhipeng on 2018/1/19.
 * 自定义的实体类，获取wifi基本信息的返回，包括了一些wifi的基本信息。
 * @see WifiBasicinfoReq
 */

public class WifibasicinfoRsp {

    private String ssid;
    private String mac;
    private int priceMode;
    private float priceInQlc;
    private int paymentType;
    private int deviceAllowed;
    private int connectCount;
    private int timeLimitPerDevice;
    private int dailyTotalTimeLimit;
    private long avaterUpdateTime;

    public long getAvaterUpdateTime() {
        return avaterUpdateTime;
    }

    public void setAvaterUpdateTime(long avaterUpdateTime) {
        this.avaterUpdateTime = avaterUpdateTime;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getPriceMode() {
        return priceMode;
    }

    public void setPriceMode(int priceMode) {
        this.priceMode = priceMode;
    }

    public float getPriceInQlc() {
        return priceInQlc;
    }

    public void setPriceInQlc(float priceInQlc) {
        this.priceInQlc = priceInQlc;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public int getDeviceAllowed() {
        return deviceAllowed;
    }

    public void setDeviceAllowed(int deviceAllowed) {
        this.deviceAllowed = deviceAllowed;
    }

    public int getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(int connectCount) {
        this.connectCount = connectCount;
    }

    public int getTimeLimitPerDevice() {
        return timeLimitPerDevice;
    }

    public void setTimeLimitPerDevice(int timeLimitPerDevice) {
        this.timeLimitPerDevice = timeLimitPerDevice;
    }

    public int getDailyTotalTimeLimit() {
        return dailyTotalTimeLimit;
    }

    public void setDailyTotalTimeLimit(int dailyTotalTimeLimit) {
        this.dailyTotalTimeLimit = dailyTotalTimeLimit;
    }
}
