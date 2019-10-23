package com.stratagile.qlink.entity.topup;

import com.stratagile.qlink.entity.BaseBack;

public class TopupOrder extends BaseBack<TopupOrder.OrderBean> {

    /**
     * order : {"productIspEn":"移动","originalPrice":50,"discountPrice":47.5,"productCountryEn":"中国","productIsp":"移动","userId":"bafe415310bd41fdb055fb0fe6cd1080","productName":"广东移动","number":"20190926142632718668","productProvinceEn":"广东","areaCode":"+86","phoneNumber":"15989241110","orderTime":"2019-09-26 14:26:32","productNameEn":"广东移动","productCountry":"中国","id":"826f83cf3526449488124c55359f53e1","productProvince":"广东","status":"QGAS_PAID"}
     */

    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean {
        /**
         * productIspEn : 移动
         * originalPrice : 50
         * discountPrice : 47.5
         * productCountryEn : 中国
         * productIsp : 移动
         * userId : bafe415310bd41fdb055fb0fe6cd1080
         * productName : 广东移动
         * number : 20190926142632718668
         * productProvinceEn : 广东
         * areaCode : +86
         * phoneNumber : 15989241110
         * orderTime : 2019-09-26 14:26:32
         * productNameEn : 广东移动
         * productCountry : 中国
         * id : 826f83cf3526449488124c55359f53e1
         * productProvince : 广东
         * status : QGAS_PAID
         */

        private String productIspEn;
        private int originalPrice;
        private double discountPrice;
        private String productCountryEn;
        private String productIsp;
        private String userId;
        private String productName;
        private String number;
        private String productProvinceEn;
        private String areaCode;
        private String phoneNumber;
        private String orderTime;
        private String productNameEn;
        private String productCountry;
        private String id;
        private String productProvince;
        private String status;
        private String p2pId;

        public String getP2pId() {
            return p2pId;
        }

        public void setP2pId(String p2pId) {
            this.p2pId = p2pId;
        }

        public String getProductIspEn() {
            return productIspEn;
        }

        public void setProductIspEn(String productIspEn) {
            this.productIspEn = productIspEn;
        }

        public int getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(int originalPrice) {
            this.originalPrice = originalPrice;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getProductCountryEn() {
            return productCountryEn;
        }

        public void setProductCountryEn(String productCountryEn) {
            this.productCountryEn = productCountryEn;
        }

        public String getProductIsp() {
            return productIsp;
        }

        public void setProductIsp(String productIsp) {
            this.productIsp = productIsp;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getProductProvinceEn() {
            return productProvinceEn;
        }

        public void setProductProvinceEn(String productProvinceEn) {
            this.productProvinceEn = productProvinceEn;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getProductNameEn() {
            return productNameEn;
        }

        public void setProductNameEn(String productNameEn) {
            this.productNameEn = productNameEn;
        }

        public String getProductCountry() {
            return productCountry;
        }

        public void setProductCountry(String productCountry) {
            this.productCountry = productCountry;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductProvince() {
            return productProvince;
        }

        public void setProductProvince(String productProvince) {
            this.productProvince = productProvince;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
