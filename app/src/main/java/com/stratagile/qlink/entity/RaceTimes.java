package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.stratagile.qlink.ui.adapter.wordcup.GameListAdapter;

import java.util.List;

public class RaceTimes extends BaseBack {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements MultiItemEntity, Parcelable {
        /**
         * racekey : 45375365b3784edfb1eb7e2844f76809
         * raceType : Group A
         * tiedBetsAmount : 0
         * isBegin : 0
         * homeCountryNumber : RUS
         * awayCountryName : Saudi Arabia
         * raceResult :
         * awayCountryNumber : SAU
         * homeCountryName : Russia
         * homeBetsAmount : 0
         * awayBetsAmount : 0
         * homeGoalsAmount : 0
         * raceTime : 06-14 23:00
         * awayGoalsAmount : 0
         */

        private String racekey;
        private String raceType;
        private float tiedBetsAmount;
        private int isBegin;
        private String homeCountryNumber;
        private String awayCountryName;
        private int raceResult;
        private String awayCountryNumber;
        private String homeCountryName;
        private float homeBetsAmount;
        private float awayBetsAmount;
        private int homeGoalsAmount;
        private String raceTime;
        private int awayGoalsAmount;
        private betRecordBean betRecord;

        protected DataBean(Parcel in) {
            racekey = in.readString();
            raceType = in.readString();
            tiedBetsAmount = in.readFloat();
            isBegin = in.readInt();
            homeCountryNumber = in.readString();
            awayCountryName = in.readString();
            raceResult = in.readInt();
            awayCountryNumber = in.readString();
            homeCountryName = in.readString();
            homeBetsAmount = in.readFloat();
            awayBetsAmount = in.readFloat();
            homeGoalsAmount = in.readInt();
            raceTime = in.readString();
            awayGoalsAmount = in.readInt();
            betRecord = in.readParcelable(betRecordBean.class.getClassLoader());
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public betRecordBean getBetRecord() {
            return betRecord;
        }

        public void setBetRecord(betRecordBean betRecord) {
            this.betRecord = betRecord;
        }

        public String getRacekey() {
            return racekey;
        }

        public void setRacekey(String racekey) {
            this.racekey = racekey;
        }

        public String getRaceType() {
            return raceType;
        }

        public void setRaceType(String raceType) {
            this.raceType = raceType;
        }

        public float getTiedBetsAmount() {
            return tiedBetsAmount;
        }

        public void setTiedBetsAmount(float tiedBetsAmount) {
            this.tiedBetsAmount = tiedBetsAmount;
        }

        public int getIsBegin() {
            return isBegin;
        }

        public void setIsBegin(int isBegin) {
            this.isBegin = isBegin;
        }

        public String getHomeCountryNumber() {
            return homeCountryNumber;
        }

        public void setHomeCountryNumber(String homeCountryNumber) {
            this.homeCountryNumber = homeCountryNumber;
        }

        public String getAwayCountryName() {
            return awayCountryName;
        }

        public void setAwayCountryName(String awayCountryName) {
            this.awayCountryName = awayCountryName;
        }

        public int getRaceResult() {
            return raceResult;
        }

        public void setRaceResult(int raceResult) {
            this.raceResult = raceResult;
        }

        public String getAwayCountryNumber() {
            return awayCountryNumber;
        }

        public void setAwayCountryNumber(String awayCountryNumber) {
            this.awayCountryNumber = awayCountryNumber;
        }

        public String getHomeCountryName() {
            return homeCountryName;
        }

        public void setHomeCountryName(String homeCountryName) {
            this.homeCountryName = homeCountryName;
        }

        public float getHomeBetsAmount() {
            return homeBetsAmount;
        }

        public void setHomeBetsAmount(float homeBetsAmount) {
            this.homeBetsAmount = homeBetsAmount;
        }

        public float getAwayBetsAmount() {
            return awayBetsAmount;
        }

        public void setAwayBetsAmount(int awayBetsAmount) {
            this.awayBetsAmount = awayBetsAmount;
        }

        public int getHomeGoalsAmount() {
            return homeGoalsAmount;
        }

        public void setHomeGoalsAmount(int homeGoalsAmount) {
            this.homeGoalsAmount = homeGoalsAmount;
        }

        public String getRaceTime() {
            return raceTime;
        }

        public void setRaceTime(String raceTime) {
            this.raceTime = raceTime;
        }

        public int getAwayGoalsAmount() {
            return awayGoalsAmount;
        }

        public void setAwayGoalsAmount(int awayGoalsAmount) {
            this.awayGoalsAmount = awayGoalsAmount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(racekey);
            dest.writeString(raceType);
            dest.writeFloat(tiedBetsAmount);
            dest.writeInt(isBegin);
            dest.writeString(homeCountryNumber);
            dest.writeString(awayCountryName);
            dest.writeInt(raceResult);
            dest.writeString(awayCountryNumber);
            dest.writeString(homeCountryName);
            dest.writeFloat(homeBetsAmount);
            dest.writeFloat(awayBetsAmount);
            dest.writeInt(homeGoalsAmount);
            dest.writeString(raceTime);
            dest.writeInt(awayGoalsAmount);
            dest.writeParcelable(betRecord, flags);
        }

        @Override
        public int getItemType() {
            return GameListAdapter.TYPE_CHILDREN;
        }
    }

    public static class RaceBeginTime implements MultiItemEntity{

        private String beginTime;

        public RaceBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getBeginTime() {

            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        @Override
        public int getItemType() {
            return GameListAdapter.TYPE_PARENT;
        }
    }

    public static class betRecordBean implements Parcelable{
        private float amount;
        private String address;
        private int bettingValue;
        private String bettingTime;
        private float rewardAmount;

        protected betRecordBean(Parcel in) {
            amount = in.readFloat();
            address = in.readString();
            bettingValue = in.readInt();
            bettingTime = in.readString();
            rewardAmount = in.readFloat();
        }

        public static final Creator<betRecordBean> CREATOR = new Creator<betRecordBean>() {
            @Override
            public betRecordBean createFromParcel(Parcel in) {
                return new betRecordBean(in);
            }

            @Override
            public betRecordBean[] newArray(int size) {
                return new betRecordBean[size];
            }
        };

        public float getRewardAmount() {
            return rewardAmount;
        }

        public void setRewardAmount(float rewardAmount) {
            this.rewardAmount = rewardAmount;
        }


        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBettingValue() {
            return bettingValue;
        }

        public void setBettingValue(int bettingValue) {
            this.bettingValue = bettingValue;
        }

        public String getBettingTime() {
            return bettingTime;
        }

        public void setBettingTime(String bettingTime) {
            this.bettingTime = bettingTime;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeFloat(amount);
            dest.writeString(address);
            dest.writeInt(bettingValue);
            dest.writeString(bettingTime);
            dest.writeFloat(rewardAmount);
        }
    }
}
