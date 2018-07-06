package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class VpnBasicInfoRsp {
    private String vpnName;
    private String p2pId;
    private String country;
    private int connectMaxnumber;
    private String profileLocalPath;
    private String bandwidth;
    private int currentConnect;
    private long avaterUpdateTime;
    private String continent;

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    private String ipV4Address;
    private float qlc;
    //该资产是否存在，
    private boolean exist;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getIpV4Address() {
        return ipV4Address;
    }

    public void setIpV4Address(String ipV4Address) {
        this.ipV4Address = ipV4Address;
    }

    public long getAvaterUpdateTime() {
        return avaterUpdateTime;
    }

    public void setAvaterUpdateTime(long avaterUpdateTime) {
        this.avaterUpdateTime = avaterUpdateTime;
    }

    public float getQlc() {
        return qlc;
    }

    public void setQlc(float qlc) {
        this.qlc = qlc;
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getConnectMaxnumber() {
        return connectMaxnumber;
    }

    public void setConnectMaxnumber(int connectMaxnumber) {
        this.connectMaxnumber = connectMaxnumber;
    }

    public String getProfileLocalPath() {
        return profileLocalPath;
    }

    public void setProfileLocalPath(String profileLocalPath) {
        this.profileLocalPath = profileLocalPath;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

    public int getCurrentConnect() {
        return currentConnect;
    }

    public void setCurrentConnect(int currentConnect) {
        this.currentConnect = currentConnect;
    }
}
