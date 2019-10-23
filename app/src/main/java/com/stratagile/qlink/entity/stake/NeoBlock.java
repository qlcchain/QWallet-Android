package com.stratagile.qlink.entity.stake;

import java.util.List;

public class NeoBlock {

    /**
     * error : {"code":-1,"message":"No Data","data":"Data does not exist"}
     */

    private ErrorBean error;

    @Override
    public String toString() {
        return "NeoBlock{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id=" + id +
                ", result=" + result +
                '}';
    }

    /**
     * jsonrpc : 2.0
     * id : 1
     * result : [{"blockindex":4232419,"txid":"0xaff224f3a2df486587856f8531508bc5d485b7e32d3a63ccecefe13465b75203","n":0,"asset":"0x0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5","from":"AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK","to":"AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd","value":"1"}]
     */

    private String jsonrpc;
    private int id;
    private List<ResultBean> result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class ResultBean {
        /**
         * blockindex : 4232419
         * txid : 0xaff224f3a2df486587856f8531508bc5d485b7e32d3a63ccecefe13465b75203
         * n : 0
         * asset : 0x0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5
         * from : AXa39WUxN6rXjRMt36Zs88XZi5hZHcF8GK
         * to : AQ4SCrvgeU5AzfhL4QVvy2FC2TPqUgJLBd
         * value : 1
         */

        private int blockindex;
        private String txid;
        private int n;
        private String asset;

        @Override
        public String toString() {
            return "ResultBean{" +
                    "blockindex=" + blockindex +
                    ", txid='" + txid + '\'' +
                    ", n=" + n +
                    ", asset='" + asset + '\'' +
                    ", from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }

        private String from;
        private String to;
        private String value;

        public int getBlockindex() {
            return blockindex;
        }

        public void setBlockindex(int blockindex) {
            this.blockindex = blockindex;
        }

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public static class ErrorBean {
        /**
         * code : -1
         * message : No Data
         * data : Data does not exist
         */

        private int code;
        private String message;
        private String data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
