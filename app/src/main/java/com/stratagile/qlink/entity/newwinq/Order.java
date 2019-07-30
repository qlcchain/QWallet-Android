package com.stratagile.qlink.entity.newwinq;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class Order extends BaseBack {

    /**
     * totalQlc : 27.1
     * orderList : [{"addRevenue":0,"amount":2,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-22 16:03:17","id":"053a5183064242ce902fe7dd589b066f","productName":"QLC日日盈","dueDays":1,"maturityTime":"","status":"PAY"},{"addRevenue":0,"amount":1,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-22 16:00:31","id":"2a40991bdb0d494facc239388de09dd9","productName":"QLC日日盈","dueDays":1,"maturityTime":"","status":"PAY"},{"addRevenue":0,"amount":1,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-11 18:34:25","id":"f82b2cd02ea049cca7c6b596c1203e79","productName":"QLC日日盈","dueDays":1,"maturityTime":"","status":"PAY"},{"addRevenue":0,"amount":1,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-11 18:13:21","id":"b4848ef8290846568551460834756b5d","productName":"QLC享全年","dueDays":355,"maturityTime":"2020-04-11 23:59:59","status":"PAY"},{"addRevenue":0,"amount":1,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-11 18:10:50","id":"e6bda590e96d470db22022b0e4615b62","productName":"QLC锁仓30天","dueDays":20,"maturityTime":"2019-05-12 23:59:59","status":"PAY"},{"addRevenue":0.1,"amount":20,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-11 16:51:14","id":"e04b617a7a45416eb9a210190cc421ec","productName":"QLC锁仓30天","dueDays":20,"maturityTime":"2019-05-12 23:59:59","status":"PAY"},{"addRevenue":0,"amount":1,"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","orderTime":"2019-04-11 16:37:16","id":"9e2c6ecd16e14c26895126f39bcfe314","productName":"QLC锁仓30天","dueDays":20,"maturityTime":"2019-05-12 23:59:59","status":"PAY"}]
     * totalRevenue : 0.1
     * yesterdayRevenue : 0.01
     */

    private double totalQlc;
    private double totalRevenue;
    private double yesterdayRevenue;
    private List<OrderListBean> orderList;

    public double getTotalQlc() {
        return totalQlc;
    }

    public void setTotalQlc(double totalQlc) {
        this.totalQlc = totalQlc;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getYesterdayRevenue() {
        return yesterdayRevenue;
    }

    public void setYesterdayRevenue(double yesterdayRevenue) {
        this.yesterdayRevenue = yesterdayRevenue;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * addRevenue : 0.0
         * amount : 2
         * address : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * orderTime : 2019-04-22 16:03:17
         * id : 053a5183064242ce902fe7dd589b066f
         * productName : QLC日日盈
         * dueDays : 1
         * maturityTime :
         * status : PAY
         */

        private double addRevenue;
        private int amount;
        private String address;
        private String orderTime;
        private String id;
        private String productName;
        private String productNameEn;
        private int dueDays;
        private String maturityTime;
        private String status;

        public String getProductNameEn() {
            return productNameEn;
        }

        public void setProductNameEn(String productNameEn) {
            this.productNameEn = productNameEn;
        }

        public double getAddRevenue() {
            return addRevenue;
        }

        public void setAddRevenue(double addRevenue) {
            this.addRevenue = addRevenue;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public int getDueDays() {
            return dueDays;
        }

        public void setDueDays(int dueDays) {
            this.dueDays = dueDays;
        }

        public String getMaturityTime() {
            return maturityTime;
        }

        public void setMaturityTime(String maturityTime) {
            this.maturityTime = maturityTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
