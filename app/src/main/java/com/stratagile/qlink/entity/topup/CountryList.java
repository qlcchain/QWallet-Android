package com.stratagile.qlink.entity.topup;

import com.google.gson.annotations.SerializedName;
import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class CountryList extends BaseBack {

    private ArrayList<CountryListBean> countryList;

    public ArrayList<CountryListBean> getCountryList() {
        return countryList;
    }

    public void setCountryList(ArrayList<CountryListBean> countryList) {
        this.countryList = countryList;
    }

    public static class CountryListBean {
        /**
         * code : CN
         * globalRoaming : +86
         * imgPath : /......
         * name : 中国
         * nameEn : China
         */

        @SerializedName("code")
        private String codeX;
        private String globalRoaming;
        private String imgPath;
        private String name;
        private String nameEn;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        private boolean select = false;

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
}
