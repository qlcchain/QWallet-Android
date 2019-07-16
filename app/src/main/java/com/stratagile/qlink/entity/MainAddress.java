package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

public class MainAddress extends BaseBack<MainAddress.DataBean> {

    /**
     * data : {"NEO":{"address":"AMGFAXHkrDhdWDSsVzcfcnCdUQE3rKDjo7"},"ETH":{"address":"0xfdae196edc10a085d95cf157c658d526fb94e4ae"},"QLC-CHIAN":{"address":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb"},"net":"MainNet"}
     */

    public static class DataBean {
        /**
         * NEO : {"address":"AMGFAXHkrDhdWDSsVzcfcnCdUQE3rKDjo7"}
         * ETH : {"address":"0xfdae196edc10a085d95cf157c658d526fb94e4ae"}
         * QLC-CHIAN : {"address":"qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb"}
         * net : MainNet
         */

        private NEOBean NEO;
        private ETHBean ETH;
        private QLCCHIANBean QLCCHIAN;
        private String net;

        public NEOBean getNEO() {
            return NEO;
        }

        public void setNEO(NEOBean NEO) {
            this.NEO = NEO;
        }

        public ETHBean getETH() {
            return ETH;
        }

        public void setETH(ETHBean ETH) {
            this.ETH = ETH;
        }

        public QLCCHIANBean getQLCCHIAN() {
            return QLCCHIAN;
        }

        public void setQLCCHIAN(QLCCHIANBean QLCCHIAN) {
            this.QLCCHIAN = QLCCHIAN;
        }

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }

        public static class NEOBean {
            /**
             * address : AMGFAXHkrDhdWDSsVzcfcnCdUQE3rKDjo7
             */

            private String address;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }

        public static class ETHBean {
            /**
             * address : 0xfdae196edc10a085d95cf157c658d526fb94e4ae
             */

            private String address;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }

        public static class QLCCHIANBean {
            /**
             * address : qlc_3wpp343n1kfsd4r6zyhz3byx4x74hi98r6f1es4dw5xkyq8qdxcxodia4zbb
             */

            private String address;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }
}
