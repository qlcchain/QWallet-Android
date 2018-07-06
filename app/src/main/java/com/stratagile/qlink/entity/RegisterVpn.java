package com.stratagile.qlink.entity;

public class RegisterVpn extends BaseBack{
    /**
     * vpnName : japan3
     * recordId : f64affddd1fd4214814f577f747a3973
     * country : Japan
     * p2pId : 2BF63D03CA68AA5B8BD75B80319DE58066A7251B8A6E14A0A1B5718247BC5F47FF5A275DBF35
     * address : AbLSovbur8DHHwJdwDjCqEtDERnNMNnZhA
     * type : 3
     * qlc : 1
     * registerQlc : 1
     * cost : 0.1
     * connectNum : 1
     * ipv4Address :
     * bandWidth : 0.0
     * profileLocalPath : /storage/emulated/0/Qlink/1530691086437.ovpn
     * imgUrl : /data/dapp/head/3c46a8afda924d2da4f4cc66d64b3522.jpg
     * heartTime :
     * isFirstRegister : true
     */

    private String vpnName;
    private String recordId;
    private String country;
    private String p2pId;
    private String address;
    private int type;
    private double qlc;
    private double registerQlc;
    private double cost;
    private int connectNum;
    private String ipv4Address;
    private double bandWidth;
    private String profileLocalPath;
    private String imgUrl;
    private String heartTime;
    private boolean isFirstRegister;

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getQlc() {
        return qlc;
    }

    public void setQlc(double qlc) {
        this.qlc = qlc;
    }

    public double getRegisterQlc() {
        return registerQlc;
    }

    public void setRegisterQlc(double registerQlc) {
        this.registerQlc = registerQlc;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getConnectNum() {
        return connectNum;
    }

    public void setConnectNum(int connectNum) {
        this.connectNum = connectNum;
    }

    public String getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(String ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public double getBandWidth() {
        return bandWidth;
    }

    public void setBandWidth(double bandWidth) {
        this.bandWidth = bandWidth;
    }

    public String getProfileLocalPath() {
        return profileLocalPath;
    }

    public void setProfileLocalPath(String profileLocalPath) {
        this.profileLocalPath = profileLocalPath;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(String heartTime) {
        this.heartTime = heartTime;
    }

    public boolean isIsFirstRegister() {
        return isFirstRegister;
    }

    public void setIsFirstRegister(boolean isFirstRegister) {
        this.isFirstRegister = isFirstRegister;
    }
}
