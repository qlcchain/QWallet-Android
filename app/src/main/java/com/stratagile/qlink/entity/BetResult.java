package com.stratagile.qlink.entity;

public class BetResult extends BaseBack {

    /**
     * data : {"betResult":true}
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
         * betResult : true
         */

        private boolean betResult;

        private RaceTimes.DataBean raceInfo;

        public RaceTimes.DataBean getRaceInfo() {
            return raceInfo;
        }

        public void setRaceInfo(RaceTimes.DataBean raceInfo) {
            this.raceInfo = raceInfo;
        }

        public boolean isBetResult() {
            return betResult;
        }

        public void setBetResult(boolean betResult) {
            this.betResult = betResult;
        }
    }
}
