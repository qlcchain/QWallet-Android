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
         * countryEn : Singapore
         * explain : Validity: 30 Days
         * country : Singapore
         * payTokenUsdPrice : 1.0
         * isp :  Starhub
         * payWay : TOKEN
         * description :
         * discount : 0.9
         * qgasDiscount : 0.05
         * payTokenSymbol : QLC
         * payFiatAmount : 3.68
         * province :
         * payTokenChain : NEO_CHAIN
         * explainEn : Validity: 30 Days
         * id : a83bf1e7cd86413b896a1783a43b750f
         * stock : 9999
         * descriptionEn :
         * localFiat : SGD
         * localFiatAmount : 5
         * nameEn : Starhub - Happy Card
         * orderTimes : 1
         * payTokenHash : 0x0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5
         * upTime : 2019-12-27 18:34:53
         * ispEn :  Starhub
         * payFiat : USD
         * payTokenCnyPrice : 1.0
         * imgPath : /userfiles/1/images/topup/2019/12/a83bf1e7cd86413b896a1783a43b751f.png
         * name : Starhub - Happy Card
         * haveGroupBuy : yes
         * items : [{"head":"/data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg","nickname":"hzpa","id":"bafe415310bd41fdb055fb0fe6cd1080"}]
         * provinceEn :
         */

        private String countryEn;
        private String explain;
        private String country;
        private double payTokenUsdPrice;
        private String isp;
        private String payWay;
        private String description;
        private double discount;
        private double qgasDiscount;
        private String payTokenSymbol;
        private String payFiatAmount;
        private String province;
        private String payTokenChain;
        private String explainEn;
        private String id;
        private int stock;
        private String descriptionEn;
        private String localFiat;
        private String localFiatAmount;
        private String nameEn;
        private int orderTimes;
        private String payTokenHash;
        private String upTime;
        private String ispEn;
        private String payFiat;
        private double payTokenCnyPrice;
        private String imgPath;
        private String name;
        private String haveGroupBuy;
        private String provinceEn;
        private List<ItemsBean> items;

        protected ProductListBean(Parcel in) {
            countryEn = in.readString();
            explain = in.readString();
            country = in.readString();
            payTokenUsdPrice = in.readDouble();
            isp = in.readString();
            payWay = in.readString();
            description = in.readString();
            discount = in.readDouble();
            qgasDiscount = in.readDouble();
            payTokenSymbol = in.readString();
            payFiatAmount = in.readString();
            province = in.readString();
            payTokenChain = in.readString();
            explainEn = in.readString();
            id = in.readString();
            stock = in.readInt();
            descriptionEn = in.readString();
            localFiat = in.readString();
            localFiatAmount = in.readString();
            nameEn = in.readString();
            orderTimes = in.readInt();
            payTokenHash = in.readString();
            upTime = in.readString();
            ispEn = in.readString();
            payFiat = in.readString();
            payTokenCnyPrice = in.readDouble();
            imgPath = in.readString();
            name = in.readString();
            haveGroupBuy = in.readString();
            provinceEn = in.readString();
            items = in.createTypedArrayList(ItemsBean.CREATOR);
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

        public double getPayTokenUsdPrice() {
            return payTokenUsdPrice;
        }

        public void setPayTokenUsdPrice(double payTokenUsdPrice) {
            this.payTokenUsdPrice = payTokenUsdPrice;
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

        public double getQgasDiscount() {
            return qgasDiscount;
        }

        public void setQgasDiscount(double qgasDiscount) {
            this.qgasDiscount = qgasDiscount;
        }

        public String getPayTokenSymbol() {
            return payTokenSymbol;
        }

        public void setPayTokenSymbol(String payTokenSymbol) {
            this.payTokenSymbol = payTokenSymbol;
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

        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
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

        public String getLocalFiatAmount() {
            return localFiatAmount;
        }

        public void setLocalFiatAmount(String localFiatAmount) {
            this.localFiatAmount = localFiatAmount;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public int getOrderTimes() {
            return orderTimes;
        }

        public void setOrderTimes(int orderTimes) {
            this.orderTimes = orderTimes;
        }

        public String getPayTokenHash() {
            return payTokenHash;
        }

        public void setPayTokenHash(String payTokenHash) {
            this.payTokenHash = payTokenHash;
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

        public String getPayFiat() {
            return payFiat;
        }

        public void setPayFiat(String payFiat) {
            this.payFiat = payFiat;
        }

        public double getPayTokenCnyPrice() {
            return payTokenCnyPrice;
        }

        public void setPayTokenCnyPrice(double payTokenCnyPrice) {
            this.payTokenCnyPrice = payTokenCnyPrice;
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

        public String getHaveGroupBuy() {
            return haveGroupBuy;
        }

        public void setHaveGroupBuy(String haveGroupBuy) {
            this.haveGroupBuy = haveGroupBuy;
        }

        public String getProvinceEn() {
            return provinceEn;
        }

        public void setProvinceEn(String provinceEn) {
            this.provinceEn = provinceEn;
        }

        public List<ItemsBean> getItems() {
            return items;
        }

        public void setItems(List<ItemsBean> items) {
            this.items = items;
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
            dest.writeDouble(payTokenUsdPrice);
            dest.writeString(isp);
            dest.writeString(payWay);
            dest.writeString(description);
            dest.writeDouble(discount);
            dest.writeDouble(qgasDiscount);
            dest.writeString(payTokenSymbol);
            dest.writeString(payFiatAmount);
            dest.writeString(province);
            dest.writeString(payTokenChain);
            dest.writeString(explainEn);
            dest.writeString(id);
            dest.writeInt(stock);
            dest.writeString(descriptionEn);
            dest.writeString(localFiat);
            dest.writeString(localFiatAmount);
            dest.writeString(nameEn);
            dest.writeInt(orderTimes);
            dest.writeString(payTokenHash);
            dest.writeString(upTime);
            dest.writeString(ispEn);
            dest.writeString(payFiat);
            dest.writeDouble(payTokenCnyPrice);
            dest.writeString(imgPath);
            dest.writeString(name);
            dest.writeString(haveGroupBuy);
            dest.writeString(provinceEn);
            dest.writeTypedList(items);
        }

        public static class ItemsBean implements Parcelable{
            /**
             * head : /data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg
             * nickname : hzpa
             * id : bafe415310bd41fdb055fb0fe6cd1080
             */

            private String head;
            private String nickname;
            private String id;

            protected ItemsBean(Parcel in) {
                head = in.readString();
                nickname = in.readString();
                id = in.readString();
            }

            public static final Creator<ItemsBean> CREATOR = new Creator<ItemsBean>() {
                @Override
                public ItemsBean createFromParcel(Parcel in) {
                    return new ItemsBean(in);
                }

                @Override
                public ItemsBean[] newArray(int size) {
                    return new ItemsBean[size];
                }
            };

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(head);
                dest.writeString(nickname);
                dest.writeString(id);
            }
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


    }

}
