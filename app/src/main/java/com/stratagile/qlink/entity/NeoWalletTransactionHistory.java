package com.stratagile.qlink.entity;

import java.util.List;

public class NeoWalletTransactionHistory extends BaseBack<List<NeoWalletTransactionHistory.DataBean>> {

//    private List<DataBean> data;
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * symbol : GAS
         * amount : 0
         * address_to : AbLSovbur8DHHwJdwDjCqEtDERnNMNnZhA
         * txid : 5890b0d8ead5a4f2091590c10bc16301e612aa1a7c15a5017be1049535fdc88a
         * time : 1541564375
         * block_height : 2932563
         * asset : 602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7
         * address_from : AbLSovbur8DHHwJdwDjCqEtDERnNMNnZhA
         */

        private String symbol;
        private String amount;
        private String address_to;
        private String txid;
        private int time;
        private int block_height;
        private String asset;
        private String address_from;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAddress_to() {
            return address_to;
        }

        public void setAddress_to(String address_to) {
            this.address_to = address_to;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getBlock_height() {
            return block_height;
        }

        public void setBlock_height(int block_height) {
            this.block_height = block_height;
        }

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getAddress_from() {
            return address_from;
        }

        public void setAddress_from(String address_from) {
            this.address_from = address_from;
        }
    }
}
