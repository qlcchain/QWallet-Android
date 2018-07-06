package com.stratagile.qlink.entity;

import com.stratagile.qlink.data.Assets;

import java.util.List;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class AssetsWarpper extends BaseBack{

    /**
     * code : 0
     * msg : Request success
     * data : {"GAS":{"balance":10.53,"unspent":[{"index":1,"txid":"b94c3fe4282dbeb25d7736f84ee5598b7d818399e2f2c74d95b94d396c289c38","value":10},{"index":0,"txid":"a643e44acec780832117fd24e9c3408bbf5f94690ff1ab50c38ada6a2742992f","value":0.5},{"index":0,"txid":"14c1fa28a9bcb81df19da0807bd37c2c5940287b943008d74b167b8607ff49ad","value":0.01},{"index":0,"txid":"137903fe6a9544878aec6cd16961f95e87c364d6c7d247e10ee57d2a6f4c9106","value":0.01},{"index":0,"txid":"c8e27bb19b543d34ec919cc2180376b28f5fc43acff790719a8beb4cad28992c","value":0.01},{"index":0,"txid":"a9b19b13cb249bdff16267b9361a33281c10d50d25dea80e081d1902235ede4a","value":1.0E-8}]},"NEO":{"balance":45,"unspent":[{"index":1,"txid":"1492d6e48ef391b5707305db5db660329d55178dd611033deae014ebf2a23333","value":41},{"index":0,"txid":"3fa2b3b78d8b447005eb9f7c7df763d6c478eff7b867ba5b55c18e12db184d51","value":1},{"index":0,"txid":"df83dc9fcfebca12729fa73b70cb0fb6ba8ea7e17a23511ccf74eb75440f1757","value":1},{"index":0,"txid":"f480baec22cf8dc79691ccfe845eb6cbc0b21565bb8f36639fe579e9b58a832d","value":1},{"index":0,"txid":"2c42ccb08b1eb1c7dc4d34e7fb9fe235f680dfefd7c71299c238995bf2d9b134","value":1}]},"address":"AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo","net":"TestNet"}
     */

    private Assets data;


    public Assets getData() {
        return data;
    }

    public void setData(Assets data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * GAS : {"balance":10.53,"unspent":[{"index":1,"txid":"b94c3fe4282dbeb25d7736f84ee5598b7d818399e2f2c74d95b94d396c289c38","value":10},{"index":0,"txid":"a643e44acec780832117fd24e9c3408bbf5f94690ff1ab50c38ada6a2742992f","value":0.5},{"index":0,"txid":"14c1fa28a9bcb81df19da0807bd37c2c5940287b943008d74b167b8607ff49ad","value":0.01},{"index":0,"txid":"137903fe6a9544878aec6cd16961f95e87c364d6c7d247e10ee57d2a6f4c9106","value":0.01},{"index":0,"txid":"c8e27bb19b543d34ec919cc2180376b28f5fc43acff790719a8beb4cad28992c","value":0.01},{"index":0,"txid":"a9b19b13cb249bdff16267b9361a33281c10d50d25dea80e081d1902235ede4a","value":1.0E-8}]}
         * NEO : {"balance":45,"unspent":[{"index":1,"txid":"1492d6e48ef391b5707305db5db660329d55178dd611033deae014ebf2a23333","value":41},{"index":0,"txid":"3fa2b3b78d8b447005eb9f7c7df763d6c478eff7b867ba5b55c18e12db184d51","value":1},{"index":0,"txid":"df83dc9fcfebca12729fa73b70cb0fb6ba8ea7e17a23511ccf74eb75440f1757","value":1},{"index":0,"txid":"f480baec22cf8dc79691ccfe845eb6cbc0b21565bb8f36639fe579e9b58a832d","value":1},{"index":0,"txid":"2c42ccb08b1eb1c7dc4d34e7fb9fe235f680dfefd7c71299c238995bf2d9b134","value":1}]}
         * address : AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo
         * net : TestNet
         */

        private GASBean GAS;
        private NEOBean NEO;
        private String address;
        private String net;

        public GASBean getGAS() {
            return GAS;
        }

        public void setGAS(GASBean GAS) {
            this.GAS = GAS;
        }

        public NEOBean getNEO() {
            return NEO;
        }

        public void setNEO(NEOBean NEO) {
            this.NEO = NEO;
        }

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

        public static class GASBean {
            /**
             * balance : 10.53
             * unspent : [{"index":1,"txid":"b94c3fe4282dbeb25d7736f84ee5598b7d818399e2f2c74d95b94d396c289c38","value":10},{"index":0,"txid":"a643e44acec780832117fd24e9c3408bbf5f94690ff1ab50c38ada6a2742992f","value":0.5},{"index":0,"txid":"14c1fa28a9bcb81df19da0807bd37c2c5940287b943008d74b167b8607ff49ad","value":0.01},{"index":0,"txid":"137903fe6a9544878aec6cd16961f95e87c364d6c7d247e10ee57d2a6f4c9106","value":0.01},{"index":0,"txid":"c8e27bb19b543d34ec919cc2180376b28f5fc43acff790719a8beb4cad28992c","value":0.01},{"index":0,"txid":"a9b19b13cb249bdff16267b9361a33281c10d50d25dea80e081d1902235ede4a","value":1.0E-8}]
             */

            private double balance;
            private List<UnspentBean> unspent;

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public List<UnspentBean> getUnspent() {
                return unspent;
            }

            public void setUnspent(List<UnspentBean> unspent) {
                this.unspent = unspent;
            }

            public static class UnspentBean {
                /**
                 * index : 1
                 * txid : b94c3fe4282dbeb25d7736f84ee5598b7d818399e2f2c74d95b94d396c289c38
                 * value : 10.0
                 */

                private int index;
                private String txid;
                private double value;

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public String getTxid() {
                    return txid;
                }

                public void setTxid(String txid) {
                    this.txid = txid;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }
        }

        public static class NEOBean {
            /**
             * balance : 45
             * unspent : [{"index":1,"txid":"1492d6e48ef391b5707305db5db660329d55178dd611033deae014ebf2a23333","value":41},{"index":0,"txid":"3fa2b3b78d8b447005eb9f7c7df763d6c478eff7b867ba5b55c18e12db184d51","value":1},{"index":0,"txid":"df83dc9fcfebca12729fa73b70cb0fb6ba8ea7e17a23511ccf74eb75440f1757","value":1},{"index":0,"txid":"f480baec22cf8dc79691ccfe845eb6cbc0b21565bb8f36639fe579e9b58a832d","value":1},{"index":0,"txid":"2c42ccb08b1eb1c7dc4d34e7fb9fe235f680dfefd7c71299c238995bf2d9b134","value":1}]
             */

            private int balance;
            private List<UnspentBeanX> unspent;

            public int getBalance() {
                return balance;
            }

            public void setBalance(int balance) {
                this.balance = balance;
            }

            public List<UnspentBeanX> getUnspent() {
                return unspent;
            }

            public void setUnspent(List<UnspentBeanX> unspent) {
                this.unspent = unspent;
            }

            public static class UnspentBeanX {
                /**
                 * index : 1
                 * txid : 1492d6e48ef391b5707305db5db660329d55178dd611033deae014ebf2a23333
                 * value : 41
                 */

                private int index;
                private String txid;
                private int value;

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public String getTxid() {
                    return txid;
                }

                public void setTxid(String txid) {
                    this.txid = txid;
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
}
