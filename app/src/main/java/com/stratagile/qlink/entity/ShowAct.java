package com.stratagile.qlink.entity;

public class ShowAct extends BaseBack<ShowAct.DataBean> {

    /**
     * data : {"isShow":1}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * isShow : 1
         */

        private int isShow;

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }
    }
}
