package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.stratagile.qlink.entity.topup.PayToken;

import java.util.List;

public class IndexInterface extends BaseBack {

    /**
     * tradeMiningList : [{"totalRewardAmount":100000,"descriptionEn":"每日交易挖矿得奖励, 每日结算","imgPath":"","name":"交易挖矿","description":"每日交易挖矿得奖励, 每日结算","tradeToken":"","startTime":"2019-11-19 00:00:00","id":"6ee48fe7cc554ced8c1f3526f2e989ee","nameEn":"交易挖矿","rewardToken":"","endTime":"2019-11-28 23:59:59"}]
     * currentTimeMillis : 1585717345265
     * burnQgasList : [{"unitPrice":4.5,"descriptionEn":"为节点招募提供动力，为QGas找到短期价值。","minAmount":1,"sellQgasCap":2,"description":"为节点招募提供动力，为QGas找到短期价值。","nameEn":"QGas Buyback abd Burn Program","qgasAmountTotal":20000,"tradeTokenChain":"QLC_CHAIN","imgPath":"","qgasReceiveAddress":"qlc_3fn7dsybngcf3ieoynyqox1xo8rx8haxh97tuq6f96erne7h844z7jt3x3h1","name":"QGas Buyback abd Burn Program","payTokenChain":"NEO_CHAIN","tradeToken":"QGAS","startTime":"2020-03-13 18:59:00","id":"845b6779a7c84fa889c865bbfda897b9","endTime":"2020-03-23 18:59:01","maxAmount":1000,"status":"UP","payToken":"QLC"}]
     * payTokenList : [{"symbol":"QGAS","chain":"QLC_CHAIN","usdPrice":0.045,"price":1,"id":"0bba9abd1b9d4eea9c1b5032a0d5257f","logo_png":"","decimal":8,"hash":"ea842234e4dc5b17c33b35f99b5b86111a3af0bd8e4a8822602b866711de6d81","logo_webp":""},{"symbol":"OKB","chain":"ETH_CHAIN","usdPrice":5.63,"price":39.43,"id":"25d073543212425b8f175ba2412ff3a0","logo_png":"https://s1.bqiapp.com/image/20181113/okb_mid.png","decimal":18,"hash":"0x75231f58b43240c9718dd58b4967c5114342a86c","logo_webp":"https://s1.bqiapp.com/image/20181113/okb_webpmid.webp"}]
     * countryList : [{"code":"CN","globalRoaming":"+86","imgPath":"/data/dapp/flag/china.png","name":"中国","nameEn":"China"},{"code":"SGP","globalRoaming":"+65","imgPath":"/data/dapp/flag/singapore.png","name":"Singapore","nameEn":"Singapore"},{"code":"IDN","globalRoaming":"+62","imgPath":"/data/dapp/flag/indonesia.png","name":"Indonesia","nameEn":"Indonesia"}]
     * dictList : {"burnQgasVoteStartDate":"2020-02-26 00:00:00","burnQgasVoteEndDate":"2020-03-28 23:59:59","topopGroupEndDate":"2020-02-16 23:59:59","topupGroupStartDate":"2020-01-30 00:00:00"}
     * groupKindList : [{"duration":180,"name":"满3人9折团","discount":0.9,"id":"887867843fd44424af4032a77ea51cda","nameEn":"10% off, 3 discount partners","numberOfPeople":3},{"duration":540,"name":"满5人8折团","discount":0.8,"id":"b52fcd76793f421abcbddb7b14fc11c7","nameEn":"20% off, 5 discount partners","numberOfPeople":5},{"duration":1440,"name":"满8人7折团","discount":0.7,"id":"c7df329cf36b43efb9e451522d4e3e9b","nameEn":"30% off, 8 discount partners","numberOfPeople":8}]
     */

    private long currentTimeMillis;
    private DictListBean dictList;
    private List<TradeMiningListBean> tradeMiningList;
    private List<BurnQgasListBean> burnQgasList;
    private List<PayToken.PayTokenListBean> payTokenList;
    private List<CountryListBean> countryList;
    private List<GroupKindListBean> groupKindList;

    public long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public DictListBean getDictList() {
        return dictList;
    }

    public void setDictList(DictListBean dictList) {
        this.dictList = dictList;
    }

    public List<TradeMiningListBean> getTradeMiningList() {
        return tradeMiningList;
    }

    public void setTradeMiningList(List<TradeMiningListBean> tradeMiningList) {
        this.tradeMiningList = tradeMiningList;
    }

    public List<BurnQgasListBean> getBurnQgasList() {
        return burnQgasList;
    }

    public void setBurnQgasList(List<BurnQgasListBean> burnQgasList) {
        this.burnQgasList = burnQgasList;
    }

    public List<PayToken.PayTokenListBean> getPayTokenList() {
        return payTokenList;
    }

    public void setPayTokenList(List<PayToken.PayTokenListBean> payTokenList) {
        this.payTokenList = payTokenList;
    }

    public List<CountryListBean> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryListBean> countryList) {
        this.countryList = countryList;
    }

    public List<GroupKindListBean> getGroupKindList() {
        return groupKindList;
    }

    public void setGroupKindList(List<GroupKindListBean> groupKindList) {
        this.groupKindList = groupKindList;
    }

    public static class DictListBean {
        /**
         * burnQgasVoteStartDate : 2020-02-26 00:00:00
         * burnQgasVoteEndDate : 2020-03-28 23:59:59
         * topopGroupEndDate : 2020-02-16 23:59:59
         * topupGroupStartDate : 2020-01-30 00:00:00
         */

        private String burnQgasVoteStartDate;
        private String burnQgasVoteEndDate;
        private String topopGroupEndDate;
        private String topupGroupStartDate;
        private String winq_invite_reward_amount;

        public String getWinq_invite_reward_amount() {
            return winq_invite_reward_amount;
        }

        public void setWinq_invite_reward_amount(String winq_invite_reward_amount) {
            this.winq_invite_reward_amount = winq_invite_reward_amount;
        }

        public String getBurnQgasVoteStartDate() {
            return burnQgasVoteStartDate;
        }

        public void setBurnQgasVoteStartDate(String burnQgasVoteStartDate) {
            this.burnQgasVoteStartDate = burnQgasVoteStartDate;
        }

        public String getBurnQgasVoteEndDate() {
            return burnQgasVoteEndDate;
        }

        public void setBurnQgasVoteEndDate(String burnQgasVoteEndDate) {
            this.burnQgasVoteEndDate = burnQgasVoteEndDate;
        }

        public String getTopopGroupEndDate() {
            return topopGroupEndDate;
        }

        public void setTopopGroupEndDate(String topopGroupEndDate) {
            this.topopGroupEndDate = topopGroupEndDate;
        }

        public String getTopupGroupStartDate() {
            return topupGroupStartDate;
        }

        public void setTopupGroupStartDate(String topupGroupStartDate) {
            this.topupGroupStartDate = topupGroupStartDate;
        }
    }

    public static class TradeMiningListBean {
        /**
         * totalRewardAmount : 100000.0
         * descriptionEn : 每日交易挖矿得奖励, 每日结算
         * imgPath :
         * name : 交易挖矿
         * description : 每日交易挖矿得奖励, 每日结算
         * tradeToken :
         * startTime : 2019-11-19 00:00:00
         * id : 6ee48fe7cc554ced8c1f3526f2e989ee
         * nameEn : 交易挖矿
         * rewardToken :
         * endTime : 2019-11-28 23:59:59
         */

        private double totalRewardAmount;
        private String descriptionEn;
        private String imgPath;
        private String name;
        private String description;
        private String tradeToken;
        private String startTime;
        private String id;
        private String nameEn;
        private String rewardToken;
        private String endTime;

        public double getTotalRewardAmount() {
            return totalRewardAmount;
        }

        public void setTotalRewardAmount(double totalRewardAmount) {
            this.totalRewardAmount = totalRewardAmount;
        }

        public String getDescriptionEn() {
            return descriptionEn;
        }

        public void setDescriptionEn(String descriptionEn) {
            this.descriptionEn = descriptionEn;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTradeToken() {
            return tradeToken;
        }

        public void setTradeToken(String tradeToken) {
            this.tradeToken = tradeToken;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public String getRewardToken() {
            return rewardToken;
        }

        public void setRewardToken(String rewardToken) {
            this.rewardToken = rewardToken;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public static class BurnQgasListBean {
        /**
         * unitPrice : 4.5
         * descriptionEn : 为节点招募提供动力，为QGas找到短期价值。
         * minAmount : 1.0
         * sellQgasCap : 2.0
         * description : 为节点招募提供动力，为QGas找到短期价值。
         * nameEn : QGas Buyback abd Burn Program
         * qgasAmountTotal : 20000.0
         * tradeTokenChain : QLC_CHAIN
         * imgPath :
         * qgasReceiveAddress : qlc_3fn7dsybngcf3ieoynyqox1xo8rx8haxh97tuq6f96erne7h844z7jt3x3h1
         * name : QGas Buyback abd Burn Program
         * payTokenChain : NEO_CHAIN
         * tradeToken : QGAS
         * startTime : 2020-03-13 18:59:00
         * id : 845b6779a7c84fa889c865bbfda897b9
         * endTime : 2020-03-23 18:59:01
         * maxAmount : 1000.0
         * status : UP
         * payToken : QLC
         */

        private double unitPrice;
        private String descriptionEn;
        private double minAmount;
        private double sellQgasCap;
        private String description;
        private String nameEn;
        private double qgasAmountTotal;
        private String tradeTokenChain;
        private String imgPath;
        private String qgasReceiveAddress;
        private String name;
        private String payTokenChain;
        private String tradeToken;
        private String startTime;
        private String id;
        private String endTime;
        private double maxAmount;
        private String status;
        private String payToken;

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getDescriptionEn() {
            return descriptionEn;
        }

        public void setDescriptionEn(String descriptionEn) {
            this.descriptionEn = descriptionEn;
        }

        public double getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(double minAmount) {
            this.minAmount = minAmount;
        }

        public double getSellQgasCap() {
            return sellQgasCap;
        }

        public void setSellQgasCap(double sellQgasCap) {
            this.sellQgasCap = sellQgasCap;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public double getQgasAmountTotal() {
            return qgasAmountTotal;
        }

        public void setQgasAmountTotal(double qgasAmountTotal) {
            this.qgasAmountTotal = qgasAmountTotal;
        }

        public String getTradeTokenChain() {
            return tradeTokenChain;
        }

        public void setTradeTokenChain(String tradeTokenChain) {
            this.tradeTokenChain = tradeTokenChain;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getQgasReceiveAddress() {
            return qgasReceiveAddress;
        }

        public void setQgasReceiveAddress(String qgasReceiveAddress) {
            this.qgasReceiveAddress = qgasReceiveAddress;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPayTokenChain() {
            return payTokenChain;
        }

        public void setPayTokenChain(String payTokenChain) {
            this.payTokenChain = payTokenChain;
        }

        public String getTradeToken() {
            return tradeToken;
        }

        public void setTradeToken(String tradeToken) {
            this.tradeToken = tradeToken;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public double getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(double maxAmount) {
            this.maxAmount = maxAmount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPayToken() {
            return payToken;
        }

        public void setPayToken(String payToken) {
            this.payToken = payToken;
        }
    }

    public static class PayTokenListBean implements Parcelable {
        /**
         * symbol : QGAS
         * chain : QLC_CHAIN
         * usdPrice : 0.045
         * price : 1.0
         * id : 0bba9abd1b9d4eea9c1b5032a0d5257f
         * logo_png :
         * decimal : 8
         * hash : ea842234e4dc5b17c33b35f99b5b86111a3af0bd8e4a8822602b866711de6d81
         * logo_webp :
         */

        private String symbol;
        private String chain;
        private double usdPrice;
        private double price;
        private String id;
        private String logo_png;
        private int decimal;
        private String hash;
        private String logo_webp;

        protected PayTokenListBean(Parcel in) {
            symbol = in.readString();
            chain = in.readString();
            usdPrice = in.readDouble();
            price = in.readDouble();
            id = in.readString();
            logo_png = in.readString();
            decimal = in.readInt();
            hash = in.readString();
            logo_webp = in.readString();
        }

        public static final Creator<PayTokenListBean> CREATOR = new Creator<PayTokenListBean>() {
            @Override
            public PayTokenListBean createFromParcel(Parcel in) {
                return new PayTokenListBean(in);
            }

            @Override
            public PayTokenListBean[] newArray(int size) {
                return new PayTokenListBean[size];
            }
        };

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public double getUsdPrice() {
            return usdPrice;
        }

        public void setUsdPrice(double usdPrice) {
            this.usdPrice = usdPrice;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogo_png() {
            return logo_png;
        }

        public void setLogo_png(String logo_png) {
            this.logo_png = logo_png;
        }

        public int getDecimal() {
            return decimal;
        }

        public void setDecimal(int decimal) {
            this.decimal = decimal;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getLogo_webp() {
            return logo_webp;
        }

        public void setLogo_webp(String logo_webp) {
            this.logo_webp = logo_webp;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(symbol);
            dest.writeString(chain);
            dest.writeDouble(usdPrice);
            dest.writeDouble(price);
            dest.writeString(id);
            dest.writeString(logo_png);
            dest.writeInt(decimal);
            dest.writeString(hash);
            dest.writeString(logo_webp);
        }
    }

    public static class CountryListBean {
        /**
         * code : CN
         * globalRoaming : +86
         * imgPath : /data/dapp/flag/china.png
         * name : 中国
         * nameEn : China
         */

        @SerializedName("code")
        private String codeX;
        private String globalRoaming;
        private String imgPath;
        private String name;
        private String nameEn;

        public String getCodeX() {
            return codeX;
        }

        public void setCodeX(String codeX) {
            this.codeX = codeX;
        }

        public String getGlobalRoaming() {
            return globalRoaming;
        }

        public void setGlobalRoaming(String globalRoaming) {
            this.globalRoaming = globalRoaming;
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

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }
    }

    public static class GroupKindListBean {
        /**
         * duration : 180
         * name : 满3人9折团
         * discount : 0.9
         * id : 887867843fd44424af4032a77ea51cda
         * nameEn : 10% off, 3 discount partners
         * numberOfPeople : 3
         */

        private int duration;
        private String name;
        private double discount;
        private String id;
        private String nameEn;
        private int numberOfPeople;

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNameEn() {
            return nameEn;
        }

        public void setNameEn(String nameEn) {
            this.nameEn = nameEn;
        }

        public int getNumberOfPeople() {
            return numberOfPeople;
        }

        public void setNumberOfPeople(int numberOfPeople) {
            this.numberOfPeople = numberOfPeople;
        }
    }
}
