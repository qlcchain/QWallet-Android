package com.stratagile.qlink.entity;

import java.util.List;

public class Active extends BaseBack {

    /**
     * data : {"currentDate":"2018-08-02 09:24:00","acts":[{"actEndDate":"2018-08-03 17:00:00","actStatus":"NEW","actStartDate":"2018-08-02 12:00:00","actId":"1","actName":"奖池瓜分","actAmount":2300}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "currentDate='" + currentDate + '\'' +
                    ", acts=" + acts +
                    '}';
        }

        /**
         * currentDate : 2018-08-02 09:24:00
         * acts : [{"actEndDate":"2018-08-03 17:00:00","actStatus":"NEW","actStartDate":"2018-08-02 12:00:00","actId":"1","actName":"奖池瓜分","actAmount":2300}]
         */

        private String currentDate;
        private List<ActsBean> acts;

        public String getCurrentDate() {
            return currentDate;
        }

        public void setCurrentDate(String currentDate) {
            this.currentDate = currentDate;
        }

        public List<ActsBean> getActs() {
            return acts;
        }

        public void setActs(List<ActsBean> acts) {
            this.acts = acts;
        }

        public static class ActsBean {
            /**
             * actEndDate : 2018-08-03 17:00:00
             * actStatus : NEW
             * actStartDate : 2018-08-02 12:00:00
             * actId : 1
             * actName : 奖池瓜分
             * actAmount : 2300
             */

            private String actEndDate;
            private String actStatus;
            private String actStartDate;

            @Override
            public String toString() {
                return "ActsBean{" +
                        "actEndDate='" + actEndDate + '\'' +
                        ", actStatus='" + actStatus + '\'' +
                        ", actStartDate='" + actStartDate + '\'' +
                        ", actId='" + actId + '\'' +
                        ", actName='" + actName + '\'' +
                        ", actAmount=" + actAmount +
                        '}';
            }

            private String actId;
            private String actName;
            private int actAmount;

            public String getActEndDate() {
                return actEndDate;
            }

            public void setActEndDate(String actEndDate) {
                this.actEndDate = actEndDate;
            }

            public String getActStatus() {
                return actStatus;
            }

            public void setActStatus(String actStatus) {
                this.actStatus = actStatus;
            }

            public String getActStartDate() {
                return actStartDate;
            }

            public void setActStartDate(String actStartDate) {
                this.actStartDate = actStartDate;
            }

            public String getActId() {
                return actId;
            }

            public void setActId(String actId) {
                this.actId = actId;
            }

            public String getActName() {
                return actName;
            }

            public void setActName(String actName) {
                this.actName = actName;
            }

            public int getActAmount() {
                return actAmount;
            }

            public void setActAmount(int actAmount) {
                this.actAmount = actAmount;
            }
        }
    }
}
