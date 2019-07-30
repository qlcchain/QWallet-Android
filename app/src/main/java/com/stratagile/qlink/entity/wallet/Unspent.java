package com.stratagile.qlink.entity.wallet;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

/**
 * Created by huzhipeng on 2018/4/26.
 */

public class Unspent extends BaseBack<List<Unspent.DataBean>>{


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
         * block :
         * createTime :
         * spentBlock :
         * spentTime :
         * txid : 0x1492d6e48ef391b5707305db5db660329d55178dd611033deae014ebf2a23333
         * vout : {"Address":"AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo","Asset":"0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7","N":1,"Value":41}
         */

        private String block;
        private String createTime;
        private String spentBlock;
        private String spentTime;
        private String txid;
        private VoutBean vout;

        public String getBlock() {
            return block;
        }

        public void setBlock(String block) {
            this.block = block;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSpentBlock() {
            return spentBlock;
        }

        public void setSpentBlock(String spentBlock) {
            this.spentBlock = spentBlock;
        }

        public String getSpentTime() {
            return spentTime;
        }

        public void setSpentTime(String spentTime) {
            this.spentTime = spentTime;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public VoutBean getVout() {
            return vout;
        }

        public void setVout(VoutBean vout) {
            this.vout = vout;
        }

        public static class VoutBean {
            /**
             * Address : AQi5CC1aAH5cjEXpX8v4i2wJrpLzdCcsuo
             * Asset : 0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7
             * N : 1
             * Value : 41
             */

            private String Address;
            private String Asset;
            private int N;
            private int Value;

            public String getAddress() {
                return Address;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public String getAsset() {
                return Asset;
            }

            public void setAsset(String Asset) {
                this.Asset = Asset;
            }

            public int getN() {
                return N;
            }

            public void setN(int N) {
                this.N = N;
            }

            public int getValue() {
                return Value;
            }

            public void setValue(int Value) {
                this.Value = Value;
            }
        }
    }
}
