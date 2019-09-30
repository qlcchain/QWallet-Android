package com.stratagile.qlink.entity.stake;

public class PledgeInfo {

    /**
     * result : {"amount":"100000000","nep5TxId":"e368453394060367056b6c1d33b492e4d0ac8171cb4b208680b9b8e038c3b779","beneficial":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","pledgeTime":1569752416,"lastModifyTime":1569752416,"pType":"vote","pledge":"qlc_3gfm3jzqe1qas5tq43c3tnxncq74ogcmry5soztgzo3tphq7barrxotq346f","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","withdrawTime":1570616404,"state":"PledgeDone","qgas":11411}
     * id : 4c6e12b915254b9e9c9d78c2904dcd0e
     * jsonrpc : 2.0
     */

    private ResultBean result;
    private String id;
    private String jsonrpc;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

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

    public static class ResultBean {
        /**
         * amount : 100000000
         * nep5TxId : e368453394060367056b6c1d33b492e4d0ac8171cb4b208680b9b8e038c3b779
         * beneficial : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * pledgeTime : 1569752416
         * lastModifyTime : 1569752416
         * pType : vote
         * pledge : qlc_3gfm3jzqe1qas5tq43c3tnxncq74ogcmry5soztgzo3tphq7barrxotq346f
         * multiSigAddress : AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd
         * withdrawTime : 1570616404
         * state : PledgeDone
         * qgas : 11411
         */

        private String amount;
        private String nep5TxId;
        private String beneficial;
        private int pledgeTime;
        private int lastModifyTime;
        private String pType;
        private String pledge;
        private String multiSigAddress;
        private int withdrawTime;
        private String state;
        private int qgas;

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

        public int getPledgeTime() {
            return pledgeTime;
        }

        public void setPledgeTime(int pledgeTime) {
            this.pledgeTime = pledgeTime;
        }

        public int getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(int lastModifyTime) {
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

        public int getWithdrawTime() {
            return withdrawTime;
        }

        public void setWithdrawTime(int withdrawTime) {
            this.withdrawTime = withdrawTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getQgas() {
            return qgas;
        }

        public void setQgas(int qgas) {
            this.qgas = qgas;
        }
    }
}
