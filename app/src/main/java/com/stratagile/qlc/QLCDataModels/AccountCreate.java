package com.stratagile.qlc.QLCDataModels;

public class AccountCreate {

    @Override
    public String toString() {
        return "AccountCreate{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", id=" + id +
                ", result=" + result +
                '}';
    }

    /**
     * jsonrpc : 2.0
     * id : 3
     * result : {"privKey":"f59e77456e068c5e4384776c9a6bbfd774abb7bdd99aa072a3304e40599fd658c39010e6c0a9d53a3e83f3a36970b660257f000ee940648d6cdfbc1d7a932b71","pubKey":"c39010e6c0a9d53a3e83f3a36970b660257f000ee940648d6cdfbc1d7a932b71"}
     */

    private String jsonrpc;
    private int id;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * privKey : f59e77456e068c5e4384776c9a6bbfd774abb7bdd99aa072a3304e40599fd658c39010e6c0a9d53a3e83f3a36970b660257f000ee940648d6cdfbc1d7a932b71
         * pubKey : c39010e6c0a9d53a3e83f3a36970b660257f000ee940648d6cdfbc1d7a932b71
         */

        private String privKey;
        private String pubKey;

        @Override
        public String toString() {
            return "ResultBean{" +
                    "privKey='" + privKey + '\'' +
                    ", pubKey='" + pubKey + '\'' +
                    '}';
        }

        public String getPrivKey() {
            return privKey;
        }

        public void setPrivKey(String privKey) {
            this.privKey = privKey;
        }

        public String getPubKey() {
            return pubKey;
        }

        public void setPubKey(String pubKey) {
            this.pubKey = pubKey;
        }
    }
}
