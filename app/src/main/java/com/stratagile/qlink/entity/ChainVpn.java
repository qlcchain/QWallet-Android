package com.stratagile.qlink.entity;

import java.util.List;

/**
 * Created by huzhipeng on 2018/2/8.
 */

public class ChainVpn extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private List<VpnListBean> vpnList;

        public List<VpnListBean> getVpnList() {
            return vpnList;
        }

        public void setVpnList(List<VpnListBean> vpnList) {
            this.vpnList = vpnList;
        }

        public static class VpnListBean {
            /**
             * vpnName : Brooklyn Vpn
             * country : Canada
             * p2pId : 873090A26E537710C345DDBA986D340183B79B5606E4069DFBA18BE47566DC03F5B7D51E5D53
             * address : ARq1rG1QKo9tJBZoptFD3zVutSxPvC966G
             * type : 3
             * qlc : 813.2
             * registerQlc : 0.0
             * cost : 0.0
             * connectNum : 0
             * bandWidth : 0.0
             * imgUrl :
             * heartTime :
             */

            private String vpnName;
            private String country;
            private String p2pId;
            private String address;
            private int type;
            private double qlc;
            private double registerQlc;
            private double cost;
            private int connectNum;
            private double bandWidth;

            public int getOnlineTime() {
                return onlineTime;
            }

            public void setOnlineTime(int onlineTime) {
                this.onlineTime = onlineTime;
            }

            public int getConnsuccessNum() {
                return connsuccessNum;
            }

            public void setConnsuccessNum(int connsuccessNum) {
                this.connsuccessNum = connsuccessNum;
            }

            private int onlineTime;
            private int connsuccessNum;

            public String getProfileLocalPath() {
                return profileLocalPath;
            }

            public void setProfileLocalPath(String profileLocalPath) {
                this.profileLocalPath = profileLocalPath;
            }

            private String imgUrl;
            private String heartTime;
            private String profileLocalPath;

            public String getVpnName() {
                return vpnName;
            }

            public void setVpnName(String vpnName) {
                this.vpnName = vpnName;
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

            public double getBandWidth() {
                return bandWidth;
            }

            public void setBandWidth(double bandWidth) {
                this.bandWidth = bandWidth;
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
        }
    }
}
