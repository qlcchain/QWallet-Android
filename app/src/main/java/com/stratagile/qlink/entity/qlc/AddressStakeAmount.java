package com.stratagile.qlink.entity.qlc;

public class AddressStakeAmount {

    /**
     * result : {"TotalAmounts":0}
     * id : 686f677fb2a541fa91213ddf8b591c4b
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
         * TotalAmounts : 0
         */

        private long TotalAmounts;

        public long getTotalAmounts() {
            return TotalAmounts;
        }

        public void setTotalAmounts(long TotalAmounts) {
            this.TotalAmounts = TotalAmounts;
        }
    }
}
