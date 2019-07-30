package com.stratagile.qlink.entity;

import java.util.List;

public class ActiveList extends BaseBack<ActiveList.DataBean> {

    /**
     * data : {"actStatus":"START","vpnRanking":[{"imgUrl":"/data/dapp/head/ff1b71a06b264278b7b70deb9d1163fe.jpg","assetName":"StevenNL","connectSuccessNum":4},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"Finland_vpn","connectSuccessNum":3},{"imgUrl":"/data/dapp/head/182fef1c66f54c1683e74bf37045a537.jpg","assetName":"PrivateNL","connectSuccessNum":2},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"Japan_vpn_1","connectSuccessNum":1},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"Japan_vpn_2","connectSuccessNum":1},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"germany_vpn","connectSuccessNum":1},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"vpn_test1","connectSuccessNum":1}]}
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
         * actStatus : START
         * vpnRanking : [{"imgUrl":"/data/dapp/head/ff1b71a06b264278b7b70deb9d1163fe.jpg","assetName":"StevenNL","connectSuccessNum":4},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"Finland_vpn","connectSuccessNum":3},{"imgUrl":"/data/dapp/head/182fef1c66f54c1683e74bf37045a537.jpg","assetName":"PrivateNL","connectSuccessNum":2},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"Japan_vpn_1","connectSuccessNum":1},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"Japan_vpn_2","connectSuccessNum":1},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"germany_vpn","connectSuccessNum":1},{"imgUrl":"/data/dapp/head/802d994df59a480697edc0f41e404c9c.jpg","assetName":"vpn_test1","connectSuccessNum":1}]
         */

        private String actStatus;

        public String getActId() {
            return actId;
        }

        public void setActId(String actId) {
            this.actId = actId;
        }

        private String actId;
        private List<VpnRankingBean> vpnRanking;

        public String getActStatus() {
            return actStatus;
        }

        public void setActStatus(String actStatus) {
            this.actStatus = actStatus;
        }

        public List<VpnRankingBean> getVpnRanking() {
            return vpnRanking;
        }

        public void setVpnRanking(List<VpnRankingBean> vpnRanking) {
            this.vpnRanking = vpnRanking;
        }

        public static class VpnRankingBean {
            /**
             * imgUrl : /data/dapp/head/ff1b71a06b264278b7b70deb9d1163fe.jpg
             * assetName : StevenNL
             * connectSuccessNum : 4
             */

            private double rewardTotal;
            private double totalQlc;

            public double getTotalQlc() {
                return totalQlc;
            }

            public void setTotalQlc(double totalQlc) {
                this.totalQlc = totalQlc;
            }

            public double getRewardTotal() {
                return rewardTotal;
            }

            public void setRewardTotal(double rewardTotal) {
                this.rewardTotal = rewardTotal;
            }

            private String imgUrl;
            private String assetName;
            private int connectSuccessNum;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getAssetName() {
                return assetName;
            }

            public void setAssetName(String assetName) {
                this.assetName = assetName;
            }

            public int getConnectSuccessNum() {
                return connectSuccessNum;
            }

            public void setConnectSuccessNum(int connectSuccessNum) {
                this.connectSuccessNum = connectSuccessNum;
            }
        }
    }
}
