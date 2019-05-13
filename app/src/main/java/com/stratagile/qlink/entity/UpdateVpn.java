package com.stratagile.qlink.entity;

/**
 * Created by zl on 2018/07/11.
 */

public class UpdateVpn extends BaseBack<UpdateVpn.DataBean> {


    /**
     * data : {"country":"Others"}
     */

//    private DataBean data;
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * country : Others
         */

        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
