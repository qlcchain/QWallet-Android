package com.stratagile.qlink.entity;

import java.util.List;

public class NeoWalletInfo extends BaseBack {

    /**
     * data : {"address":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","balance":[{"unspent":[{"txid":"9645a78f138a7edc95b5423031eded4f8b739dfb5c66730903c4f42cbd946655","value":1.0E-8,"n":0},{"txid":"3134028bd45b100bf99cd7d2f66f7a63386bc9fe964e6295de6307aef39d7060","value":0.5,"n":0},{"txid":"9ea7c1c694c3a241a7ce7a2932cb5101b5199eab81ccad25284d0f07d5457c95","value":0.47999999,"n":1},{"txid":"e39d3e21dc902a196559e5145628fd4bcd781b2e24af4b24c23f4db468f0a4a7","value":10.53,"n":0},{"txid":"e8ea7b6c0446fb86dc6aef4a430c47f42cf3d6070238595d1b937dfc0ae692a9","value":0.5,"n":0}],"amount":12.01,"asset_hash":"602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7","asset_symbol":"GAS","asset":"GAS"},{"unspent":[],"amount":7725.26,"asset_hash":"b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263","asset_symbol":"QLC","asset":"Qlink Token"},{"unspent":[{"txid":"1e707f52318c17533674d81991871782c819ab972eda5991c1426323e2719678","value":43,"n":0}],"amount":43,"asset_hash":"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","asset_symbol":"NEO","asset":"NEO"}]}
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
         * balance : [{"unspent":[{"txid":"9645a78f138a7edc95b5423031eded4f8b739dfb5c66730903c4f42cbd946655","value":1.0E-8,"n":0},{"txid":"3134028bd45b100bf99cd7d2f66f7a63386bc9fe964e6295de6307aef39d7060","value":0.5,"n":0},{"txid":"9ea7c1c694c3a241a7ce7a2932cb5101b5199eab81ccad25284d0f07d5457c95","value":0.47999999,"n":1},{"txid":"e39d3e21dc902a196559e5145628fd4bcd781b2e24af4b24c23f4db468f0a4a7","value":10.53,"n":0},{"txid":"e8ea7b6c0446fb86dc6aef4a430c47f42cf3d6070238595d1b937dfc0ae692a9","value":0.5,"n":0}],"amount":12.01,"asset_hash":"602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7","asset_symbol":"GAS","asset":"GAS"},{"unspent":[],"amount":7725.26,"asset_hash":"b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263","asset_symbol":"QLC","asset":"Qlink Token"},{"unspent":[{"txid":"1e707f52318c17533674d81991871782c819ab972eda5991c1426323e2719678","value":43,"n":0}],"amount":43,"asset_hash":"c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b","asset_symbol":"NEO","asset":"NEO"}]
         */

        private String address;
        private List<BalanceBean> balance;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<BalanceBean> getBalance() {
            return balance;
        }

        public void setBalance(List<BalanceBean> balance) {
            this.balance = balance;
        }

        public static class BalanceBean {
            /**
             * unspent : [{"txid":"9645a78f138a7edc95b5423031eded4f8b739dfb5c66730903c4f42cbd946655","value":1.0E-8,"n":0},{"txid":"3134028bd45b100bf99cd7d2f66f7a63386bc9fe964e6295de6307aef39d7060","value":0.5,"n":0},{"txid":"9ea7c1c694c3a241a7ce7a2932cb5101b5199eab81ccad25284d0f07d5457c95","value":0.47999999,"n":1},{"txid":"e39d3e21dc902a196559e5145628fd4bcd781b2e24af4b24c23f4db468f0a4a7","value":10.53,"n":0},{"txid":"e8ea7b6c0446fb86dc6aef4a430c47f42cf3d6070238595d1b937dfc0ae692a9","value":0.5,"n":0}]
             * amount : 12.01
             * asset_hash : 602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7
             * asset_symbol : GAS
             * asset : GAS
             */

            private double amount;
            private String asset_hash;
            private String asset_symbol;
            private String asset;
            private List<UnspentBean> unspent;

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public String getAsset_hash() {
                return asset_hash;
            }

            public void setAsset_hash(String asset_hash) {
                this.asset_hash = asset_hash;
            }

            public String getAsset_symbol() {
                return asset_symbol;
            }

            public void setAsset_symbol(String asset_symbol) {
                this.asset_symbol = asset_symbol;
            }

            public String getAsset() {
                return asset;
            }

            public void setAsset(String asset) {
                this.asset = asset;
            }

            public List<UnspentBean> getUnspent() {
                return unspent;
            }

            public void setUnspent(List<UnspentBean> unspent) {
                this.unspent = unspent;
            }

            public static class UnspentBean {
                /**
                 * txid : 9645a78f138a7edc95b5423031eded4f8b739dfb5c66730903c4f42cbd946655
                 * value : 1.0E-8
                 * n : 0
                 */

                private String txid;
                private double value;
                private int n;

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

                public int getN() {
                    return n;
                }

                public void setN(int n) {
                    this.n = n;
                }
            }
        }
    }
}
