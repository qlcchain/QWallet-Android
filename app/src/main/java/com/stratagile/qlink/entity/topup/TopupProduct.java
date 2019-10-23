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
         * countryEn : 中国
         * explain : 1
         * country : 中国
         * descriptionEn : 1
         * isp : 移动
         * description : 1
         * discount : 0.95
         * nameEn : 广东移动
         * amountOfMoney : 50,100,200,300,400,500
         * upTime : 2019-09-25 14:47:17
         * ispEn : 移动
         * province : 广东
         * imgPath : /
         * name : 广东移动
         * explainEn : 1
         * id : 56594c9614984f149ec93b51e5161fyy
         * provinceEn : 广东
         */

        private String countryEn;
        private String explain;
        private String country;
        private String descriptionEn;
        private String isp;
        private String description;
        private double discount;

        protected ProductListBean(Parcel in) {
            countryEn = in.readString();
            explain = in.readString();
            country = in.readString();
            descriptionEn = in.readString();
            isp = in.readString();
            description = in.readString();
            discount = in.readDouble();
            qgasDiscount = in.readDouble();
            nameEn = in.readString();
            amountOfMoney = in.readString();
            upTime = in.readString();
            ispEn = in.readString();
            province = in.readString();
            imgPath = in.readString();
            name = in.readString();
            explainEn = in.readString();
            id = in.readString();
            provinceEn = in.readString();
            price = in.readInt();
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

        public double getQgasDiscount() {
            return qgasDiscount;
        }

        public void setQgasDiscount(double qgasDiscount) {
            this.qgasDiscount = qgasDiscount;
        }

        private double qgasDiscount;
        private String nameEn;
        private String amountOfMoney;
        private String upTime;
        private String ispEn;
        private String province;
        private String imgPath;
        private String name;
        private String explainEn;
        private String id;
        private String provinceEn;
        private int price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

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

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
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

        public String getProvinceEn() {
            return provinceEn;
        }

        public void setProvinceEn(String provinceEn) {
            this.provinceEn = provinceEn;
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
            dest.writeString(isp);
            dest.writeString(description);
            dest.writeDouble(discount);
            dest.writeDouble(qgasDiscount);
            dest.writeString(nameEn);
            dest.writeString(amountOfMoney);
            dest.writeString(upTime);
            dest.writeString(ispEn);
            dest.writeString(province);
            dest.writeString(imgPath);
            dest.writeString(name);
            dest.writeString(explainEn);
            dest.writeString(id);
            dest.writeString(provinceEn);
            dest.writeInt(price);
        }
    }
}
