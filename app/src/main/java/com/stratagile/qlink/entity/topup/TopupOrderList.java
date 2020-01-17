package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;

public class TopupOrderList extends BaseBack<TopupOrderList.OrderListBean> {

    private ArrayList<TopupOrder.OrderBean> orderList;

    public ArrayList<TopupOrder.OrderBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<TopupOrder.OrderBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean implements Parcelable {


        /**
         * symbol : OKB
         * productIspEn : 通用
         * chain : ETH_CHAIN
         * originalPrice : 50.0
         * discountPrice : 49.0
         * txid : 0x200a022fb0fc8a9e2c35deb9b9e97bb5c222061a2817526d8cb1f64bc2e5bf91
         * productCountryEn : 中国
         * productIsp : 通用
         * type : SLOW
         * userId : 7060628a65e4450690976bf56c127787
         * productName : 全国通用
         * number : 20191024212002427531
         * qgasAmount : 0.056
         * productProvinceEn : 全国
         * areaCode : +86
         * phoneNumber : 18670819116
         * orderTime : 2019-10-24 21:20:03
         * productNameEn : 全国通用
         * productCountry : 中国
         * id : 6055705b2cc546319f355ff260ce6c75
         * productProvince : 全国
         * status : QGAS_PAID
         */

        private String symbol;
        private String productIspEn;
        private String chain;
        private double originalPrice;
        private double discountPrice;
        private String txid;
        private String productCountryEn;
        private String productIsp;
        private String type;
        private String userId;
        private String productName;
        private String number;
        private double qgasAmount;
        private String productProvinceEn;
        private String areaCode;
        private String phoneNumber;
        private String orderTime;
        private String productNameEn;
        private String productCountry;
        private String id;
        private String productProvince;
        private String status;

        protected OrderListBean(Parcel in) {
            symbol = in.readString();
            productIspEn = in.readString();
            chain = in.readString();
            originalPrice = in.readDouble();
            discountPrice = in.readDouble();
            txid = in.readString();
            productCountryEn = in.readString();
            productIsp = in.readString();
            type = in.readString();
            userId = in.readString();
            productName = in.readString();
            number = in.readString();
            qgasAmount = in.readDouble();
            productProvinceEn = in.readString();
            areaCode = in.readString();
            phoneNumber = in.readString();
            orderTime = in.readString();
            productNameEn = in.readString();
            productCountry = in.readString();
            id = in.readString();
            productProvince = in.readString();
            status = in.readString();
        }

        public static final Creator<OrderListBean> CREATOR = new Creator<OrderListBean>() {
            @Override
            public OrderListBean createFromParcel(Parcel in) {
                return new OrderListBean(in);
            }

            @Override
            public OrderListBean[] newArray(int size) {
                return new OrderListBean[size];
            }
        };

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getProductIspEn() {
            return productIspEn;
        }

        public void setProductIspEn(String productIspEn) {
            this.productIspEn = productIspEn;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public double getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(double qgasAmount) {
            this.qgasAmount = qgasAmount;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(symbol);
            dest.writeString(productIspEn);
            dest.writeString(chain);
            dest.writeDouble(originalPrice);
            dest.writeDouble(discountPrice);
            dest.writeString(txid);
            dest.writeString(productCountryEn);
            dest.writeString(productIsp);
            dest.writeString(type);
            dest.writeString(userId);
            dest.writeString(productName);
            dest.writeString(number);
            dest.writeDouble(qgasAmount);
            dest.writeString(productProvinceEn);
            dest.writeString(areaCode);
            dest.writeString(phoneNumber);
            dest.writeString(orderTime);
            dest.writeString(productNameEn);
            dest.writeString(productCountry);
            dest.writeString(id);
            dest.writeString(productProvince);
            dest.writeString(status);
        }
    }
}
