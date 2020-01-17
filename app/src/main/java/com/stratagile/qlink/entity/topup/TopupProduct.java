package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class TopupProduct extends BaseBack<TopupProduct.ProductListBean> {


    private List<ProductListBean> productList;

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ProductListBean implements Parcelable {
        public ProductListBean() {
        }

        /**
         * countryEn : All Operators in China
         * explain : 72小时内到账 急用勿拍
         * country : 中国
         * descriptionEn : 慢充话费，折扣低，价格实惠
         * localFiat : CNY 当地法币
         * isp : 通用  运营商
         * payWay : FIAT   支付方式，fiat= 法币 token = 代币
         * description : 慢充话费，折扣低，价格实惠
         * discount : 0.98
         * nameEn : All provinces of China
         * qgasDiscount : 0.02   抵扣币的折扣
         * amountOfMoney : 30,50,100   当地法币的金额，和支付币的金额一一对应
         * upTime : 2019-10-19 13:31:34
         * ispEn :
         * payFiatAmount :
         * province : 全国
         * payFiat : CYN  支付法币是哪一个
         * imgPath : /userfiles/1/images/topup/2019/10/3a117d16c4654ab78a332db30613dadb.jpg
         * name : 全国通用
         * explainEn : 72小时内到账 急用勿拍
         * id : 4b51058ae87141df9217b2d8de9530cc
         * stock : -1    库存
         * provinceEn :
         * payTokenSymbol : QLC 支付币，如果是法币支付，这个字段没有
         * payTokenUsdPrice : 0.0   支付币美元价格
         * payTokenCnyPrice : 0.5   支付币人民币价格
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
        private String deductionToken;
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
        private String payTokenSymbol;
        private double payTokenUsdPrice;
        private double payTokenCnyPrice;


        protected ProductListBean(Parcel in) {
            countryEn = in.readString();
            explain = in.readString();
            country = in.readString();
            descriptionEn = in.readString();
            localFiat = in.readString();
            isp = in.readString();
            payWay = in.readString();
            description = in.readString();
            discount = in.readDouble();
            nameEn = in.readString();
            qgasDiscount = in.readDouble();
            deductionToken = in.readString();
            amountOfMoney = in.readString();
            upTime = in.readString();
            ispEn = in.readString();
            payFiatAmount = in.readString();
            province = in.readString();
            payFiat = in.readString();
            imgPath = in.readString();
            name = in.readString();
            explainEn = in.readString();
            id = in.readString();
            stock = in.readInt();
            provinceEn = in.readString();
            payTokenSymbol = in.readString();
            payTokenUsdPrice = in.readDouble();
            payTokenCnyPrice = in.readDouble();
        }

        public static final Creator<ProductListBean> CREATOR = new Creator<ProductListBean>() {
            @Override
            public ProductListBean createFromParcel(Parcel in) {
                return new ProductListBean(in);
            }

            @Override
            public ProductListBean[] newArray(int size) {
                return new ProductListBean[size];
            }
        };

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

        public String getDeductionToken() {
            return deductionToken;
        }

        public void setDeductionToken(String deductionToken) {
            this.deductionToken = deductionToken;
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

        public String getPayTokenSymbol() {
            return payTokenSymbol;
        }

        public void setPayTokenSymbol(String payTokenSymbol) {
            this.payTokenSymbol = payTokenSymbol;
        }

        public double getPayTokenUsdPrice() {
            return payTokenUsdPrice;
        }

        public void setPayTokenUsdPrice(double payTokenUsdPrice) {
            this.payTokenUsdPrice = payTokenUsdPrice;
        }

        public double getPayTokenCnyPrice() {
            return payTokenCnyPrice;
        }

        public void setPayTokenCnyPrice(double payTokenCnyProce) {
            this.payTokenCnyPrice = payTokenCnyProce;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(countryEn);
            dest.writeString(explain);
            dest.writeString(country);
            dest.writeString(descriptionEn);
            dest.writeString(localFiat);
            dest.writeString(isp);
            dest.writeString(payWay);
            dest.writeString(description);
            dest.writeDouble(discount);
            dest.writeString(nameEn);
            dest.writeDouble(qgasDiscount);
            dest.writeString(deductionToken);
            dest.writeString(amountOfMoney);
            dest.writeString(upTime);
            dest.writeString(ispEn);
            dest.writeString(payFiatAmount);
            dest.writeString(province);
            dest.writeString(payFiat);
            dest.writeString(imgPath);
            dest.writeString(name);
            dest.writeString(explainEn);
            dest.writeString(id);
            dest.writeInt(stock);
            dest.writeString(provinceEn);
            dest.writeString(payTokenSymbol);
            dest.writeDouble(payTokenUsdPrice);
            dest.writeDouble(payTokenCnyPrice);
        }
    }
}
