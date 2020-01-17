package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

public class TopupOrder extends BaseBack<TopupOrder.OrderBean> {

    /**
     * order : {"symbol":"QLC","originalPrice":10,"discountPrice":9.5,"type":"SLOW","deductionPrice":0.5,"productName":"广东移动","number":"20191226163033456342","qgasAmount":0.5,"productProvinceEn":"","orderTime":"2019-12-26 16:30:33","payPrice":9,"payTokenInTxid":"","id":"c837aa44141b477f83583250afc1277f","productProvince":"广东","productIspEn":"","chain":"QLC_CHAIN","txid":"","productCountryEn":"China Mobile","productIsp":"移动","payTokenAmount":18,"userId":"7060628a65e4450690976bf56c127787","areaCode":"+86","phoneNumber":"15989246851","productNameEn":"Guangdong","productCountry":"中国","status":"NEW"}
     */

    private OrderBean order;

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public static class OrderBean implements Parcelable {

        /**
         * symbol : QGAS
         * originalPrice : 10
         * discountPrice : 9.5
         * payWay : TOKEN
         * type : SLOW
         * deductionPrice : 0.5
         * payTokenSymbol : QLC
         * productName : 广东移动
         * serialno :
         * number : 20191227172613979599
         * qgasAmount : 0.5
         * productProvinceEn :
         * orderTime : 2019-12-27 17:26:13
         * pin :
         * payPrice : 9.0
         * payTokenInTxid :
         * payTokenChain : NEO_CHAIN
         * id : 6e39def38aff4ae4a76e8d4bd87c157d
         * productProvince : 广东
         * localFiat : CNY
         * productIspEn :
         * chain : QLC_CHAIN
         * txid :
         * productCountryEn : China Mobile
         * productIsp : 移动
         * payTokenAmount : 9.0
         * userId : 7060628a65e4450690976bf56c127787
         * expiredTime :
         * payTokenHash : 0x0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5
         * areaCode : +86
         * phoneNumber : 15989241234
         * payFiat : CNY
         * passwd :
         * productNameEn : Guangdong
         * productCountry : 中国
         * hash : ea842234e4dc5b17c33b35f99b5b86111a3af0bd8e4a8822602b866711de6d81
         * status : NEW
         */

        private String symbol;
        private double originalPrice;
        private double discountPrice;
        private String payWay;
        private String type;
        private double deductionPrice;
        private String payTokenSymbol;
        private String productName;
        private String serialno;
        private String number;
        private double qgasAmount;
        private String productProvinceEn;
        private String orderTime;
        private String pin;
        private double payPrice;
        private String localFiatMoney;

        public String getLocalFiatMoney() {
            return localFiatMoney;
        }

        public void setLocalFiatMoney(String localFiatMoney) {
            this.localFiatMoney = localFiatMoney;
        }

        private String payTokenInTxid;
        private String payTokenChain;
        private String id;
        private String productProvince;
        private String localFiat;
        private String productIspEn;
        private String chain;
        private String txid;
        private String productCountryEn;
        private String productIsp;
        private String payTokenAmount;
        private String userId;
        private String expiredTime;
        private String payTokenHash;
        private String areaCode;
        private String phoneNumber;
        private String payFiat;
        private String passwd;
        private String productNameEn;
        private String productCountry;
        private String hash;
        private String status;

        public OrderBean() {
        }


        protected OrderBean(Parcel in) {
            symbol = in.readString();
            originalPrice = in.readDouble();
            discountPrice = in.readDouble();
            payWay = in.readString();
            type = in.readString();
            deductionPrice = in.readDouble();
            payTokenSymbol = in.readString();
            productName = in.readString();
            serialno = in.readString();
            number = in.readString();
            qgasAmount = in.readDouble();
            productProvinceEn = in.readString();
            orderTime = in.readString();
            pin = in.readString();
            payPrice = in.readDouble();
            localFiatMoney = in.readString();
            payTokenInTxid = in.readString();
            payTokenChain = in.readString();
            id = in.readString();
            productProvince = in.readString();
            localFiat = in.readString();
            productIspEn = in.readString();
            chain = in.readString();
            txid = in.readString();
            productCountryEn = in.readString();
            productIsp = in.readString();
            payTokenAmount = in.readString();
            userId = in.readString();
            expiredTime = in.readString();
            payTokenHash = in.readString();
            areaCode = in.readString();
            phoneNumber = in.readString();
            payFiat = in.readString();
            passwd = in.readString();
            productNameEn = in.readString();
            productCountry = in.readString();
            hash = in.readString();
            status = in.readString();
        }

        public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
            @Override
            public OrderBean createFromParcel(Parcel in) {
                return new OrderBean(in);
            }

            @Override
            public OrderBean[] newArray(int size) {
                return new OrderBean[size];
            }
        };

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
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

        public String getPayWay() {
            return payWay;
        }

        public void setPayWay(String payWay) {
            this.payWay = payWay;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getDeductionPrice() {
            return deductionPrice;
        }

        public void setDeductionPrice(double deductionPrice) {
            this.deductionPrice = deductionPrice;
        }

        public String getPayTokenSymbol() {
            return payTokenSymbol;
        }

        public void setPayTokenSymbol(String payTokenSymbol) {
            this.payTokenSymbol = payTokenSymbol;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSerialno() {
            return serialno;
        }

        public void setSerialno(String serialno) {
            this.serialno = serialno;
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

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public double getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(double payPrice) {
            this.payPrice = payPrice;
        }

        public String getPayTokenInTxid() {
            return payTokenInTxid;
        }

        public void setPayTokenInTxid(String payTokenInTxid) {
            this.payTokenInTxid = payTokenInTxid;
        }

        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
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

        public String getLocalFiat() {
            return localFiat;
        }

        public void setLocalFiat(String localFiat) {
            this.localFiat = localFiat;
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

        public String getPayTokenAmount() {
            return payTokenAmount;
        }

        public void setPayTokenAmount(String payTokenAmount) {
            this.payTokenAmount = payTokenAmount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getExpiredTime() {
            return expiredTime;
        }

        public void setExpiredTime(String expiredTime) {
            this.expiredTime = expiredTime;
        }

        public String getPayTokenHash() {
            return payTokenHash;
        }

        public void setPayTokenHash(String payTokenHash) {
            this.payTokenHash = payTokenHash;
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

        public String getPayFiat() {
            return payFiat;
        }

        public void setPayFiat(String payFiat) {
            this.payFiat = payFiat;
        }

        public String getPasswd() {
            return passwd;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
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

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
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
            dest.writeDouble(originalPrice);
            dest.writeDouble(discountPrice);
            dest.writeString(payWay);
            dest.writeString(type);
            dest.writeDouble(deductionPrice);
            dest.writeString(payTokenSymbol);
            dest.writeString(productName);
            dest.writeString(serialno);
            dest.writeString(number);
            dest.writeDouble(qgasAmount);
            dest.writeString(productProvinceEn);
            dest.writeString(orderTime);
            dest.writeString(pin);
            dest.writeDouble(payPrice);
            dest.writeString(localFiatMoney);
            dest.writeString(payTokenInTxid);
            dest.writeString(payTokenChain);
            dest.writeString(id);
            dest.writeString(productProvince);
            dest.writeString(localFiat);
            dest.writeString(productIspEn);
            dest.writeString(chain);
            dest.writeString(txid);
            dest.writeString(productCountryEn);
            dest.writeString(productIsp);
            dest.writeString(payTokenAmount);
            dest.writeString(userId);
            dest.writeString(expiredTime);
            dest.writeString(payTokenHash);
            dest.writeString(areaCode);
            dest.writeString(phoneNumber);
            dest.writeString(payFiat);
            dest.writeString(passwd);
            dest.writeString(productNameEn);
            dest.writeString(productCountry);
            dest.writeString(hash);
            dest.writeString(status);
        }
    }
}
