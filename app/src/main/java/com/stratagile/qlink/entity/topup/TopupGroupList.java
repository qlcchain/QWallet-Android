package com.stratagile.qlink.entity.topup;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class TopupGroupList extends BaseBack {


    private ArrayList<GroupListBean> groupList;

    public ArrayList<GroupListBean> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<GroupListBean> groupList) {
        this.groupList = groupList;
    }

    public static class GroupListBean implements Parcelable {
        /**
         * joined : 1
         * discount : 0.7
         * payFiatMoney : 3.68
         * deductionToken : QGAS
         * payTokenAmount : 2.45
         * deductionTokenAmount : 4.089
         * duration : 1440
         * id : f4d9fee99f924a25bc59182366b22fa1
         * payTokenPrice : 1.0
         * items : [{"head":"/data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg","nickname":"hzpa"}]
         * numberOfPeople : 8
         * status : PROCESSING
         * payToken : QLC
         * createDate : 2020-01-14 19:26:06
         */

        private int joined;
        private double discount;
        private double payFiatMoney;
        private String deductionToken;
        private double payTokenAmount;
        private double deductionTokenAmount;
        private int duration;
        private String id;
        private double payTokenPrice;
        private int numberOfPeople;

        protected GroupListBean(Parcel in) {
            joined = in.readInt();
            discount = in.readDouble();
            payFiatMoney = in.readDouble();
            deductionToken = in.readString();
            payTokenAmount = in.readDouble();
            deductionTokenAmount = in.readDouble();
            duration = in.readInt();
            id = in.readString();
            payTokenPrice = in.readDouble();
            numberOfPeople = in.readInt();
            status = in.readString();
            payToken = in.readString();
            head = in.readString();
            createDate = in.readString();
        }

        public static final Creator<GroupListBean> CREATOR = new Creator<GroupListBean>() {
            @Override
            public GroupListBean createFromParcel(Parcel in) {
                return new GroupListBean(in);
            }

            @Override
            public GroupListBean[] newArray(int size) {
                return new GroupListBean[size];
            }
        };

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        private String status;
        private String payToken;
        private String head;
        private String createDate;
        private ArrayList<ItemsBean> items;

        public int getJoined() {
            return joined;
        }

        public void setJoined(int joined) {
            this.joined = joined;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
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

        public double getDeductionTokenAmount() {
            return deductionTokenAmount;
        }

        public void setDeductionTokenAmount(double deductionTokenAmount) {
            this.deductionTokenAmount = deductionTokenAmount;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
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

        public int getNumberOfPeople() {
            return numberOfPeople;
        }

        public void setNumberOfPeople(int numberOfPeople) {
            this.numberOfPeople = numberOfPeople;
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

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public ArrayList<ItemsBean> getItems() {
            return items;
        }

        public void setItems(ArrayList<ItemsBean> items) {
            this.items = items;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(joined);
            dest.writeDouble(discount);
            dest.writeDouble(payFiatMoney);
            dest.writeString(deductionToken);
            dest.writeDouble(payTokenAmount);
            dest.writeDouble(deductionTokenAmount);
            dest.writeInt(duration);
            dest.writeString(id);
            dest.writeDouble(payTokenPrice);
            dest.writeInt(numberOfPeople);
            dest.writeString(status);
            dest.writeString(payToken);
            dest.writeString(head);
            dest.writeString(createDate);
        }

        public static class ItemsBean {
            /**
             * head : /data/dapp/head/f597e8eccf8e4624bcd92ef04d6881cb.jpg
             * nickname : hzpa
             */

            private String head;
            private String nickname;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private String id;

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
        }
    }
}
