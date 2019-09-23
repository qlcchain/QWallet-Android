package com.stratagile.qlink.entity.stake;

public class LockInfo {

    /**
     * jsonrpc : 2.0
     * id : 1
     * result : {"neoAddress":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","multiSigAddress":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","qlcAddress":"qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7","lockTimestamp":1569126045,"unLockTimestamp":1569990045,"amount":"300000000","state":true}
     */

    private String jsonrpc;
    private String id;
    private ResultBean result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * neoAddress : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * multiSigAddress : AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd
         * qlcAddress : qlc_1fyz7ksawbgak4tqfyhspsbo4udsao1x8prui9unp6ggw7rpifea6ia76pj7
         * lockTimestamp : 1569126045
         * unLockTimestamp : 1569990045
         * amount : 300000000
         * state : true
         */

        private String neoAddress;
        private String multiSigAddress;
        private String qlcAddress;
        private int lockTimestamp;
        private int unLockTimestamp;
        private String amount;
        private boolean state;

        public String getNeoAddress() {
            return neoAddress;
        }

        public void setNeoAddress(String neoAddress) {
            this.neoAddress = neoAddress;
        }

        public String getMultiSigAddress() {
            return multiSigAddress;
        }

        public void setMultiSigAddress(String multiSigAddress) {
            this.multiSigAddress = multiSigAddress;
        }

        public String getQlcAddress() {
            return qlcAddress;
        }

        public void setQlcAddress(String qlcAddress) {
            this.qlcAddress = qlcAddress;
        }

        public int getLockTimestamp() {
            return lockTimestamp;
        }

        public void setLockTimestamp(int lockTimestamp) {
            this.lockTimestamp = lockTimestamp;
        }

        public int getUnLockTimestamp() {
            return unLockTimestamp;
        }

        public void setUnLockTimestamp(int unLockTimestamp) {
            this.unLockTimestamp = unLockTimestamp;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public boolean isState() {
            return state;
        }

        public void setState(boolean state) {
            this.state = state;
        }
    }
}
