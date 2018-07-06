package com.stratagile.qlink.entity;

import java.util.ArrayList;

/**
 * Created by huzhipeng on 2018/1/10.
 */

public class WifiRegisteResult extends BaseBack {


    private ArrayList<DataBean> data;

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ssId : YYM-1
         * mac : 88:25:93:79:c1:31
         * p2pId : D7EC408C3D07BA9733DCCA2D0EACC8EBB6AD910EF5F66B0C2302494290C59A5F39CE74A1BBAA
         * address : AaUsLXgqudA3zcVvCPo5XicNQApeoejFaM
         * type : 0
         * registerQlc : 0.0
         * qlc : 221.0
         * cost : 0.0
         * connectNum : 0
         * ipv4Address :
         * bandWidth : 0.0
         * profileLocalPath :
         * imgUrl :
         */

        private String ssId;
        private String mac;
        private String p2pId;
        private String address;
        private String type;
        private double registerQlc;
        private double qlc;
        private double cost;
        private int connectNum;
//        private String ipv4Address;
//        private double bandWidth;
//        private String profileLocalPath;
        private String imgUrl;

        public String getSsId() {
            return ssId;
        }

        public void setSsId(String ssId) {
            this.ssId = ssId;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getRegisterQlc() {
            return registerQlc;
        }

        public void setRegisterQlc(double registerQlc) {
            this.registerQlc = registerQlc;
        }

        public double getQlc() {
            return qlc;
        }

        public void setQlc(double qlc) {
            this.qlc = qlc;
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

//        public String getIpv4Address() {
//            return ipv4Address;
//        }
//
//        public void setIpv4Address(String ipv4Address) {
//            this.ipv4Address = ipv4Address;
//        }
//
//        public double getBandWidth() {
//            return bandWidth;
//        }
//
//        public void setBandWidth(double bandWidth) {
//            this.bandWidth = bandWidth;
//        }
//
//        public String getProfileLocalPath() {
//            return profileLocalPath;
//        }
//
//        public void setProfileLocalPath(String profileLocalPath) {
//            this.profileLocalPath = profileLocalPath;
//        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
