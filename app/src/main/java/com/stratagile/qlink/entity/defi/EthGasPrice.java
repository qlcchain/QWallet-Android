package com.stratagile.qlink.entity.defi;

public class EthGasPrice {

    /**
     * status : 1
     * message : OK
     * result : {"LastBlock":"10735004","SafeGasPrice":"60","ProposeGasPrice":"83","FastGasPrice":"84"}
     */

    private String status;
    private String message;
    private ResultBean result;

    @Override
    public String toString() {
        return "EthGasPrice{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        @Override
        public String toString() {
            return "ResultBean{" +
                    "LastBlock='" + LastBlock + '\'' +
                    ", SafeGasPrice='" + SafeGasPrice + '\'' +
                    ", ProposeGasPrice='" + ProposeGasPrice + '\'' +
                    ", FastGasPrice='" + FastGasPrice + '\'' +
                    '}';
        }

        /**
         * LastBlock : 10735004
         * SafeGasPrice : 60
         * ProposeGasPrice : 83
         * FastGasPrice : 84
         */

        private String LastBlock;
        private String SafeGasPrice;
        private String ProposeGasPrice;
        private String FastGasPrice;

        public String getLastBlock() {
            return LastBlock;
        }

        public void setLastBlock(String LastBlock) {
            this.LastBlock = LastBlock;
        }

        public String getSafeGasPrice() {
            return SafeGasPrice;
        }

        public void setSafeGasPrice(String SafeGasPrice) {
            this.SafeGasPrice = SafeGasPrice;
        }

        public String getProposeGasPrice() {
            return ProposeGasPrice;
        }

        public void setProposeGasPrice(String ProposeGasPrice) {
            this.ProposeGasPrice = ProposeGasPrice;
        }

        public String getFastGasPrice() {
            return FastGasPrice;
        }

        public void setFastGasPrice(String FastGasPrice) {
            this.FastGasPrice = FastGasPrice;
        }
    }
}
