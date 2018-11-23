package com.stratagile.qlink.entity;

import java.util.List;

public class ClaimData extends BaseBack {

    /**
     * data : {"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","claims":[{"start":"2936848","claim":"0.000021","txid":"ee3fae2e0efcfbce0173180c34e1838ea7ed09f998c33e7333856654b6244fa6","index":0,"end":"2937148","value":1}],"net":"MainNet"}
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
         * address : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * claims : [{"start":"2936848","claim":"0.000021","txid":"ee3fae2e0efcfbce0173180c34e1838ea7ed09f998c33e7333856654b6244fa6","index":0,"end":"2937148","value":1}]
         * net : MainNet
         */

        private String address;
        private String net;
        private List<ClaimsBean> claims;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }

        public List<ClaimsBean> getClaims() {
            return claims;
        }

        public void setClaims(List<ClaimsBean> claims) {
            this.claims = claims;
        }

        public static class ClaimsBean {
            /**
             * start : 2936848
             * claim : 0.000021
             * txid : ee3fae2e0efcfbce0173180c34e1838ea7ed09f998c33e7333856654b6244fa6
             * index : 0
             * end : 2937148
             * value : 1
             */

            private String start;
            private String claim;
            private String txid;
            private int index;
            private String end;
            private int value;

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
            }

            public String getClaim() {
                return claim;
            }

            public void setClaim(String claim) {
                this.claim = claim;
            }

            public String getTxid() {
                return txid;
            }

            public void setTxid(String txid) {
                this.txid = txid;
            }

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}
