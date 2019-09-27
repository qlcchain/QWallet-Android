package com.stratagile.qlink.topup;

import java.util.ArrayList;
import java.util.List;

public class Area {

    private ArrayList<AreaBean> area;

    public ArrayList<AreaBean> getArea() {
        return area;
    }

    public void setArea(ArrayList<AreaBean> area) {
        this.area = area;
    }

    public static class AreaBean {
        /**
         * country : 中国
         * countryEn : China
         * number : 86
         */

        private String country;
        private String countryEn;
        private String number;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCountryEn() {
            return countryEn;
        }

        public void setCountryEn(String countryEn) {
            this.countryEn = countryEn;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
