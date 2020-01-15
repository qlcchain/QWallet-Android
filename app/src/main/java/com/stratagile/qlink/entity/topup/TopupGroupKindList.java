package com.stratagile.qlink.entity.topup;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class TopupGroupKindList extends BaseBack {

    private ArrayList<GroupKindListBean> groupKindList;

    public ArrayList<GroupKindListBean> getGroupKindList() {
        return groupKindList;
    }

    public void setGroupKindList(ArrayList<GroupKindListBean> groupKindList) {
        this.groupKindList = groupKindList;
    }

    public static class GroupKindListBean {
        /**
         * duration : 180
         * name : 三人团
         * discount : 0.9
         * id : 887867843fd44424af4032a77ea51cda
         * nameEn : 3 peoples
         * numberOfPeople : 3
         */

        private int duration;
        private String name;
        private double discount;
        private String id;
        private String nameEn;
        private int numberOfPeople;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        private boolean selected;

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
