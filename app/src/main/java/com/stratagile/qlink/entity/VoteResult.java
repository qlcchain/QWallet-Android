package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

public class VoteResult extends BaseBack {


    /**
     * result : {"1":1,"2":1,"3":1,"4":1}
     * choose : 2
     * opinion : ddkdk
     */

    private ResultBean result;
    private String choose;
    private String opinion;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public static class ResultBean {
        /**
         * 1 : 1
         * 2 : 1
         * 3 : 1
         * 4 : 1
         */

        @SerializedName("1")
        private int _$1;
        @SerializedName("2")
        private int _$2;
        @SerializedName("3")
        private int _$3;
        @SerializedName("4")
        private int _$4;

        public int get_$1() {
            return _$1;
        }

        public void set_$1(int _$1) {
            this._$1 = _$1;
        }

        public int get_$2() {
            return _$2;
        }

        public void set_$2(int _$2) {
            this._$2 = _$2;
        }

        public int get_$3() {
            return _$3;
        }

        public void set_$3(int _$3) {
            this._$3 = _$3;
        }

        public int get_$4() {
            return _$4;
        }

        public void set_$4(int _$4) {
            this._$4 = _$4;
        }
    }
}
