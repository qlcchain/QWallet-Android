package com.stratagile.qlink.entity;

public class MainAddress extends BaseBack {

    /**
     * data : {"NEO":{"address":"AQC7Bod2LxaRxmLewRrwCA1Nt6AQMWSm28"},"ETH":{"address":"0xfdae196edc10a085d95cf157c658d526fb94e4ae"}}
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
         * NEO : {"address":"AQC7Bod2LxaRxmLewRrwCA1Nt6AQMWSm28"}
         * ETH : {"address":"0xfdae196edc10a085d95cf157c658d526fb94e4ae"}
         */

        private NEOBean NEO;
        private ETHBean ETH;

        private String net;

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }

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

        public static class NEOBean {
            /**
             * address : AQC7Bod2LxaRxmLewRrwCA1Nt6AQMWSm28
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
    }
}
