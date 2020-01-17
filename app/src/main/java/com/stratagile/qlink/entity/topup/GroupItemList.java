package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;

public class GroupItemList extends BaseBack {

    private ArrayList<ItemListBean> itemList;

    public ArrayList<ItemListBean> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ItemListBean> itemList) {
        this.itemList = itemList;
    }

    public static class ItemListBean implements Parcelable{
        /**
         * product : {"countryEn":"Singapore","country":"Singapore","localFiat":"SGD","globalRoaming":"+65","localFiatMoney":"5","isp":" Starhub"}
         * payFiatMoney : 3.68
         * deductionToken : OKB
         * payTokenAmount : 3.146
         * userId : bafe415310bd41fdb055fb0fe6cd1080
         * deductionTokenAmount : 0.05
         * head : /data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg
         * deductionTokenInTxid : 0xf20dde2610e1d580eb82fb37d47ce98e12403bbe707f1e36b75ed5d57356556b
         * phoneNumber : 13131313131
         * payTokenInTxid :
         * payTokenChain : NEO_CHAIN
         * id : 7363769335b2489ea8422464fe6255dc
         * payTokenPrice : 1.0
         * status : NEW
         * deductionTokenChain : ETH_CHAIN
         * payToken : QLC
         * createDate : 2020-01-15 18:44:05
         */

        private ProductBean product;
        private double payFiatMoney;
        private String deductionToken;
        private double payTokenAmount;
        private String userId;
        private double deductionTokenAmount;
        private String head;
        private String deductionTokenInTxid;
        private String phoneNumber;
        private String payTokenInTxid;
        private String payTokenChain;
        private String id;
        private double payTokenPrice;
        private String status;
        private String deductionTokenChain;
        private String payToken;
        private String createDate;

        public ItemListBean() {
        }

        protected ItemListBean(Parcel in) {
            product = in.readParcelable(ProductBean.class.getClassLoader());
            payFiatMoney = in.readDouble();
            deductionToken = in.readString();
            payTokenAmount = in.readDouble();
            userId = in.readString();
            deductionTokenAmount = in.readDouble();
            head = in.readString();
            deductionTokenInTxid = in.readString();
            phoneNumber = in.readString();
            payTokenInTxid = in.readString();
            payTokenChain = in.readString();
            id = in.readString();
            payTokenPrice = in.readDouble();
            status = in.readString();
            deductionTokenChain = in.readString();
            payToken = in.readString();
            createDate = in.readString();
        }

        public static final Creator<ItemListBean> CREATOR = new Creator<ItemListBean>() {
            @Override
            public ItemListBean createFromParcel(Parcel in) {
                return new ItemListBean(in);
            }

            @Override
            public ItemListBean[] newArray(int size) {
                return new ItemListBean[size];
            }
        };

        public ProductBean getProduct() {
            return product;
        }

        public void setProduct(ProductBean product) {
            this.product = product;
        }

        public double getPayFiatMoney() {
            return payFiatMoney;
        }

        public void setPayFiatMoney(double payFiatMoney) {
            this.payFiatMoney = payFiatMoney;
        }

        public String getDeductionToken() {
            return deductionToken;
        }

        public void setDeductionToken(String deductionToken) {
            this.deductionToken = deductionToken;
        }

        public double getPayTokenAmount() {
            return payTokenAmount;
        }

        public void setPayTokenAmount(double payTokenAmount) {
            this.payTokenAmount = payTokenAmount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public double getDeductionTokenAmount() {
            return deductionTokenAmount;
        }

        public void setDeductionTokenAmount(double deductionTokenAmount) {
            this.deductionTokenAmount = deductionTokenAmount;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getDeductionTokenInTxid() {
            return deductionTokenInTxid;
        }

        public void setDeductionTokenInTxid(String deductionTokenInTxid) {
            this.deductionTokenInTxid = deductionTokenInTxid;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
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

        public double getPayTokenPrice() {
            return payTokenPrice;
        }

        public void setPayTokenPrice(double payTokenPrice) {
            this.payTokenPrice = payTokenPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeductionTokenChain() {
            return deductionTokenChain;
        }

        public void setDeductionTokenChain(String deductionTokenChain) {
            this.deductionTokenChain = deductionTokenChain;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(product, flags);
            dest.writeDouble(payFiatMoney);
            dest.writeString(deductionToken);
            dest.writeDouble(payTokenAmount);
            dest.writeString(userId);
            dest.writeDouble(deductionTokenAmount);
            dest.writeString(head);
            dest.writeString(deductionTokenInTxid);
            dest.writeString(phoneNumber);
            dest.writeString(payTokenInTxid);
            dest.writeString(payTokenChain);
            dest.writeString(id);
            dest.writeDouble(payTokenPrice);
            dest.writeString(status);
            dest.writeString(deductionTokenChain);
            dest.writeString(payToken);
            dest.writeString(createDate);
        }

        public static class ProductBean implements Parcelable {

            /**
             * countryEn : Singapore
             * country : Singapore
             * localFiat : SGD
             * ispEn :  Starhub
             * province :
             * globalRoaming : +65
             * localFiatMoney : 5
             * isp :  Starhub
             * productNameEn : Starhub - Happy Card
             * productName : Starhub - Happy Card
             * provinceEn :
             */

            private String countryEn;
            private String country;
            private String localFiat;
            private String ispEn;
            private String province;
            private String globalRoaming;
            private String localFiatMoney;
            private String isp;
            private String productNameEn;
            private String productName;
            private String provinceEn;

            protected ProductBean(Parcel in) {
                countryEn = in.readString();
                country = in.readString();
                localFiat = in.readString();
                ispEn = in.readString();
                province = in.readString();
                globalRoaming = in.readString();
                localFiatMoney = in.readString();
                isp = in.readString();
                productNameEn = in.readString();
                productName = in.readString();
                provinceEn = in.readString();
            }

            public static final Creator<ProductBean> CREATOR = new Creator<ProductBean>() {
                @Override
                public ProductBean createFromParcel(Parcel in) {
                    return new ProductBean(in);
                }

                @Override
                public ProductBean[] newArray(int size) {
                    return new ProductBean[size];
                }
            };

            public String getCountryEn() {
                return countryEn;
            }

            public void setCountryEn(String countryEn) {
                this.countryEn = countryEn;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getLocalFiat() {
                return localFiat;
            }

            public void setLocalFiat(String localFiat) {
                this.localFiat = localFiat;
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

            public String getGlobalRoaming() {
                return globalRoaming;
            }

            public void setGlobalRoaming(String globalRoaming) {
                this.globalRoaming = globalRoaming;
            }

            public String getLocalFiatMoney() {
                return localFiatMoney;
            }

            public void setLocalFiatMoney(String localFiatMoney) {
                this.localFiatMoney = localFiatMoney;
            }

            public String getIsp() {
                return isp;
            }

            public void setIsp(String isp) {
                this.isp = isp;
            }

            public String getProductNameEn() {
                return productNameEn;
            }

            public void setProductNameEn(String productNameEn) {
                this.productNameEn = productNameEn;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
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
                dest.writeString(country);
                dest.writeString(localFiat);
                dest.writeString(ispEn);
                dest.writeString(province);
                dest.writeString(globalRoaming);
                dest.writeString(localFiatMoney);
                dest.writeString(isp);
                dest.writeString(productNameEn);
                dest.writeString(productName);
                dest.writeString(provinceEn);
            }
        }
    }
}
