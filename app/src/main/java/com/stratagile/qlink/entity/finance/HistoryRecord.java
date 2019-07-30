package com.stratagile.qlink.entity.finance;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class HistoryRecord extends BaseBack {

    private List<TransactionListBean> transactionList;

    public List<TransactionListBean> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionListBean> transactionList) {
        this.transactionList = transactionList;
    }

    public static class TransactionListBean {
        /**
         * amount : 1.0
         * address : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * createTime : 2019-04-22 18:19
         * id : 4d672adcc686417bac88db5fd2e5d398
         * type : REDEEM
         * productName : QLC日日盈
         */

        private double amount;
        private String address;
        private String createTime;
        private String id;
        private String type;

        public String getProductNameEn() {
            return productNameEn;
        }

        public void setProductNameEn(String productNameEn) {
            this.productNameEn = productNameEn;
        }

        private String productName;
        private String productNameEn;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }
}
