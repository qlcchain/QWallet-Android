package com.stratagile.qlink.entity;

import com.stratagile.qlink.data.UTXO;
import com.stratagile.qlink.data.UTXOS;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class AssetsWarpper1 extends BaseBack<UTXOS>{


//    private UTXOS data;
//
//    public UTXOS getData() {
//        return data;
//    }
//
//    public void setData(UTXOS data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * createdAtBlock : 0
         * index : 1
         * txid : 0x702fddcac1852de121e3380323b15c2544d7cb6e3433759ddfc14ce4cc71afef
         * asset : 0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7
         * value : 9.9999E-4
         */

        private int createdAtBlock;
        private int index;
        private String txid;
        private String asset;
        private double value;

        public int getCreatedAtBlock() {
            return createdAtBlock;
        }

        public void setCreatedAtBlock(int createdAtBlock) {
            this.createdAtBlock = createdAtBlock;
        }

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

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
