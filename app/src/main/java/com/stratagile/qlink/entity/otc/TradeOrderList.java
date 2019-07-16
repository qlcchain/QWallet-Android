package com.stratagile.qlink.entity.otc;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class TradeOrderList extends BaseBack<TradeOrderList.OrderListBean> {

    private ArrayList<OrderListBean> orderList;

    public ArrayList<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * unitPrice : 0.001
         * usdtAmount : 0.1
         * qgasAmount : 100.0
         * sellerId : 61be9c09c0784827af303005f983c705
         * entrustOrderId : f71e12acd7ef4765ae1399213719f982
         * id : fa1db0d9493240b78c9305236ee864f7
         * buyerId : 7060628a65e4450690976bf56c127787
         * status : QGAS_TO_PLATFORM
         */

        private double unitPrice;
        private double usdtAmount;
        private double qgasAmount;
        private String sellerId;
        private String entrustOrderId;
        private String id;
        private String buyerId;
        private String status;

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getUsdtAmount() {
            return usdtAmount;
        }

        public void setUsdtAmount(double usdtAmount) {
            this.usdtAmount = usdtAmount;
        }

        public double getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(double qgasAmount) {
            this.qgasAmount = qgasAmount;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getEntrustOrderId() {
            return entrustOrderId;
        }

        public void setEntrustOrderId(String entrustOrderId) {
            this.entrustOrderId = entrustOrderId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
