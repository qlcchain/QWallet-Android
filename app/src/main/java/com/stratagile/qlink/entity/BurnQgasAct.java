package com.stratagile.qlink.entity;

import java.util.List;

public class BurnQgasAct extends BaseBack {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * unitPrice : 2.0
         * descriptionEn : 为节点招募提供动力，为QGas找到短期价值。
         * minAmount : 1.0
         * sellQgasCap : 2.0
         * description : 为节点招募提供动力，为QGas找到短期价值。
         * nameEn : QGas Buyback abd Burn Program
         * qgasAmountTotal : 200000.0
         * tradeTokenChain : QLC_CHAIN
         * imgPath :
         * qgasReceiveAddress : qlc_3fn7dsybngcf3ieoynyqox1xo8rx8haxh97tuq6f96erne7h844z7jt3x3h1
         * name : QGas Buyback abd Burn Program
         * payTokenChain : NEO_CHAIN
         * tradeToken : QGAS
         * startTime : 2020-02-27 00:00:00
         * id : 845b6779a7c84fa889c865bbfda897b9
         * endTime : 2020-03-07 23:59:59
         * maxAmount : 1000.0
         * status : UP
         * payToken : QLC
         */

        private double unitPrice;
        private String descriptionEn;
        private double minAmount;
        private double sellQgasCap;
        private String description;
        private String nameEn;
        private double qgasAmountTotal;
        private String tradeTokenChain;
        private String imgPath;
        private String qgasReceiveAddress;
        private String name;
        private String payTokenChain;
        private String tradeToken;
        private String startTime;
        private String id;
        private String endTime;
        private double maxAmount;
        private String status;
        private String payToken;

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getDescriptionEn() {
            return descriptionEn;
        }

        public void setDescriptionEn(String descriptionEn) {
            this.descriptionEn = descriptionEn;
        }

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public double getSellQgasCap() {
            return sellQgasCap;
        }

        public void setSellQgasCap(double sellQgasCap) {
            this.sellQgasCap = sellQgasCap;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public double getQgasAmountTotal() {
            return qgasAmountTotal;
        }

        public void setQgasAmountTotal(double qgasAmountTotal) {
            this.qgasAmountTotal = qgasAmountTotal;
        }

        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getQgasReceiveAddress() {
            return qgasReceiveAddress;
        }

        public void setQgasReceiveAddress(String qgasReceiveAddress) {
            this.qgasReceiveAddress = qgasReceiveAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
        }

        public String getTradeToken() {
            return tradeToken;
        }

        public void setTradeToken(String tradeToken) {
            this.tradeToken = tradeToken;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }
    }
}
