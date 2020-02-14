package com.stratagile.qlink.entity.topup;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class ProductListV2 extends BaseBack {

    private List<ProductListBean> productList;

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ProductListBean {
        /**
         * countryEn : All Operators in China
         * explain : 72小时内到账 急用勿拍
         * country : 中国
         * descriptionEn : 慢充话费，折扣低，价格实惠
         * localFiat : CNY
         * isp : 通用
         * payWay : FIAT
         * description : 慢充话费，折扣低，价格实惠
         * discount : 0.98
         * nameEn : All provinces of China
         * qgasDiscount : 0.02
         * amountOfMoney : 30,50,100
         * upTime : 2019-10-19 13:31:34
         * ispEn :
         * payFiatAmount : 30,50,100
         * province : 全国
         * payFiat : CNY
         * imgPath : /userfiles/1/images/topup/2019/10/3a117d16c4654ab78a332db30613dadb.jpg
         * name : 全国通用
         * explainEn : 72小时内到账 急用勿拍
         * id : 4b51058ae87141df9217b2d8de9530cc
         * stock : -1
         * provinceEn :
         */

        private String countryEn;
        private String explain;
        private String country;
        private String descriptionEn;
        private String localFiat;
        private String isp;
        private String payWay;
        private String description;
        private double discount;
        private String nameEn;
        private double qgasDiscount;
        private String amountOfMoney;
        private String upTime;
        private String ispEn;
        private String payFiatAmount;
        private String province;
        private String payFiat;
        private String imgPath;
        private String name;
        private String explainEn;
        private String id;
        private int stock;
        private String provinceEn;

        public String getCountryEn() {
            return countryEn;
        }

        public void setCountryEn(String countryEn) {
            this.countryEn = countryEn;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDescriptionEn() {
            return descriptionEn;
        }

        public void setDescriptionEn(String descriptionEn) {
            this.descriptionEn = descriptionEn;
        }

        public String getLocalFiat() {
            return localFiat;
        }

        public void setLocalFiat(String localFiat) {
            this.localFiat = localFiat;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getPayWay() {
            return payWay;
        }

        public void setPayWay(String payWay) {
            this.payWay = payWay;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public double getQgasDiscount() {
            return qgasDiscount;
        }

        public void setQgasDiscount(double qgasDiscount) {
            this.qgasDiscount = qgasDiscount;
        }

        public String getAmountOfMoney() {
            return amountOfMoney;
        }

        public void setAmountOfMoney(String amountOfMoney) {
            this.amountOfMoney = amountOfMoney;
        }

        public String getUpTime() {
            return upTime;
        }

        public void setUpTime(String upTime) {
            this.upTime = upTime;
        }

        public String getIspEn() {
            return ispEn;
        }

        public void setIspEn(String ispEn) {
            this.ispEn = ispEn;
        }

        public String getPayFiatAmount() {
            return payFiatAmount;
        }

        public void setPayFiatAmount(String payFiatAmount) {
            this.payFiatAmount = payFiatAmount;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getPayFiat() {
            return payFiat;
        }

        public void setPayFiat(String payFiat) {
            this.payFiat = payFiat;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getExplainEn() {
            return explainEn;
        }

        public void setExplainEn(String explainEn) {
            this.explainEn = explainEn;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getProvinceEn() {
            return provinceEn;
        }

        public void setProvinceEn(String provinceEn) {
            this.provinceEn = provinceEn;
        }
    }
}
