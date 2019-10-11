package com.stratagile.qlink.entity.reward;

import com.google.gson.annotations.SerializedName;
import com.stratagile.qlink.entity.BaseBack;

public class Dict extends BaseBack<Dict.DataBean> {

    /**
     * data : {"value":"1500"}
     */

    public static class DataBean {
        /**
         * value : 1500
         */

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
