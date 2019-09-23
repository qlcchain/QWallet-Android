package com.stratagile.qlink.entity.stake;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MyStakeList {

    /**
     * result : [{"amount":"100000000","nep5TxId":"eac10877c222d3e6aad52abd3c4ed9201afda52a5971901a40fd8f714b893e59","beneficial":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","pledgeTime":1567646579,"lastModifyTime":1567646579,"pType":"vote","pledge":"qlc_3gfm3jzqe1qas5tq43c3tnxncq74ogcmry5soztgzo3tphq7barrxotq346f","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","withdrawTime":1568431969,"state":"PledgeDone","qgas":0},{"amount":"100000000","nep5TxId":"aff224f3a2df486587856f8531508bc5d485b7e32d3a63ccecefe13465b75203","beneficial":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","pledgeTime":1567646551,"lastModifyTime":1567646551,"pType":"vote","pledge":"qlc_178gc7sgefmfbmn1fi8uqhecwyewt6wu1y9rko1fb9snu89uupm1moc65gxu","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","withdrawTime":1568510547,"state":"PledgeDone","qgas":0},{"amount":"100000000","nep5TxId":"515269433faf56f5b14ee83a7aae8f9abf11538001953d63b3dbf009f9a33141","beneficial":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","pledgeTime":1567646517,"lastModifyTime":1567646517,"pType":"vote","pledge":"qlc_3gfm3jzqe1qas5tq43c3tnxncq74ogcmry5soztgzo3tphq7barrxotq346f","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","withdrawTime":1568510515,"state":"PledgeDone","qgas":0},{"amount":"100000000","nep5TxId":"7f8170cdf8717e4b5be0a858bb4e23be68f029e1f349a2c6b0adfe52604c4468","beneficial":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","pledgeTime":1567575395,"lastModifyTime":1567575395,"pType":"vote","pledge":"qlc_3gfm3jzqe1qas5tq43c3tnxncq74ogcmry5soztgzo3tphq7barrxotq346f","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","withdrawTime":1568439387,"state":"PledgeDone","qgas":10945},{"amount":"100000000","nep5TxId":"93bb766445bf0af35c6efc9ed20f7856155a63d7af479ccec6f16eb9c448f0b9","beneficial":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","pledgeTime":1567569405,"lastModifyTime":1567569405,"pType":"vote","pledge":"qlc_178gc7sgefmfbmn1fi8uqhecwyewt6wu1y9rko1fb9snu89uupm1moc65gxu","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","withdrawTime":1568433084,"state":"PledgeDone","qgas":10945}]
     * id : a4bf825b03324ba6b378626b833f21c4
     * jsonrpc : 2.0
     */

    private String id;
    private String jsonrpc;
    private List<ResultBean> result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean implements Parcelable {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "amount='" + amount + '\'' +
                    ", nep5TxId='" + nep5TxId + '\'' +
                    ", beneficial='" + beneficial + '\'' +
                    ", pledgeTime=" + pledgeTime +
                    ", lastModifyTime=" + lastModifyTime +
                    ", pType='" + pType + '\'' +
                    ", pledge='" + pledge + '\'' +
                    ", multiSigAddress='" + multiSigAddress + '\'' +
                    ", withdrawTime=" + withdrawTime +
                    ", state='" + state + '\'' +
                    ", qgas=" + qgas +
                    '}';
        }

        /**
         * amount : 100000000
         * nep5TxId : eac10877c222d3e6aad52abd3c4ed9201afda52a5971901a40fd8f714b893e59
         * beneficial : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * pledgeTime : 1567646579
         * lastModifyTime : 1567646579
         * pType : vote
         * pledge : qlc_3gfm3jzqe1qas5tq43c3tnxncq74ogcmry5soztgzo3tphq7barrxotq346f
         * multiSigAddress : AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd
         * withdrawTime : 1568431969
         * state : PledgeDone
         * qgas : 0
         */

        private String amount;
        private String nep5TxId;
        private String beneficial;
        private long pledgeTime;
        private long lastModifyTime;
        private String pType;
        private String pledge;
        private String multiSigAddress;
        private long withdrawTime;
        private String state;
        private long qgas;

        protected ResultBean(Parcel in) {
            amount = in.readString();
            nep5TxId = in.readString();
            beneficial = in.readString();
            pledgeTime = in.readLong();
            lastModifyTime = in.readLong();
            pType = in.readString();
            pledge = in.readString();
            multiSigAddress = in.readString();
            withdrawTime = in.readLong();
            state = in.readString();
            qgas = in.readLong();
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getNep5TxId() {
            return nep5TxId;
        }

        public void setNep5TxId(String nep5TxId) {
            this.nep5TxId = nep5TxId;
        }

        public String getBeneficial() {
            return beneficial;
        }

        public void setBeneficial(String beneficial) {
            this.beneficial = beneficial;
        }

        public long getPledgeTime() {
            return pledgeTime;
        }

        public void setPledgeTime(long pledgeTime) {
            this.pledgeTime = pledgeTime;
        }

        public long getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(long lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
        }

        public String getPType() {
            return pType;
        }

        public void setPType(String pType) {
            this.pType = pType;
        }

        public String getPledge() {
            return pledge;
        }

        public void setPledge(String pledge) {
            this.pledge = pledge;
        }

        public String getMultiSigAddress() {
            return multiSigAddress;
        }

        public void setMultiSigAddress(String multiSigAddress) {
            this.multiSigAddress = multiSigAddress;
        }

        public long getWithdrawTime() {
            return withdrawTime;
        }

        public void setWithdrawTime(long withdrawTime) {
            this.withdrawTime = withdrawTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getQgas() {
            return qgas;
        }

        public void setQgas(long qgas) {
            this.qgas = qgas;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(amount);
            parcel.writeString(nep5TxId);
            parcel.writeString(beneficial);
            parcel.writeLong(pledgeTime);
            parcel.writeLong(lastModifyTime);
            parcel.writeString(pType);
            parcel.writeString(pledge);
            parcel.writeString(multiSigAddress);
            parcel.writeLong(withdrawTime);
            parcel.writeString(state);
            parcel.writeLong(qgas);
        }
    }
}
