package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EosAccountTransaction extends BaseBack<EosAccountTransaction.DataBeanX> {

    /**
     * data : {"errno":0,"data":{"trace_count":22,"trace_list":[{"trx_id":"dfab30f059c8180d6aa04db5818486cc67daaa35a63b6d58d47a53eadb8be4f3","symbol":"HVT","code":"hirevibeshvt","quantity":"0.8775","receiver":"yfhuangeos55","sender":"airdropsdac5","memo":"*HireVibes (HVT) https://www.hirevibes.io/ Token Airdrop and Claim opportunity has started. For more info visit: https://goo.gl/nrELgi | To claim, use our website Claim Tool OR go to: https://goo.gl/em1x3o OR Transfer any amount of HVT to any account.*","timestamp":"2018-11-05T05:26:28.500","status":"executed"}]},"errmsg":"Success"}
     */

//    private DataBeanX data;
//
//    public DataBeanX getData() {
//        return data;
//    }
//
//    public void setData(DataBeanX data) {
//        this.data = data;
//    }

    public static class DataBeanX {
        /**
         * errno : 0
         * data : {"trace_count":22,"trace_list":[{"trx_id":"dfab30f059c8180d6aa04db5818486cc67daaa35a63b6d58d47a53eadb8be4f3","symbol":"HVT","code":"hirevibeshvt","quantity":"0.8775","receiver":"yfhuangeos55","sender":"airdropsdac5","memo":"*HireVibes (HVT) https://www.hirevibes.io/ Token Airdrop and Claim opportunity has started. For more info visit: https://goo.gl/nrELgi | To claim, use our website Claim Tool OR go to: https://goo.gl/em1x3o OR Transfer any amount of HVT to any account.*","timestamp":"2018-11-05T05:26:28.500","status":"executed"}]}
         * errmsg : Success
         */

        private int errno;
        private DataBean data;
        private String errmsg;

        public int getErrno() {
            return errno;
        }

        public void setErrno(int errno) {
            this.errno = errno;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }

        public static class DataBean {
            /**
             * trace_count : 22
             * trace_list : [{"trx_id":"dfab30f059c8180d6aa04db5818486cc67daaa35a63b6d58d47a53eadb8be4f3","symbol":"HVT","code":"hirevibeshvt","quantity":"0.8775","receiver":"yfhuangeos55","sender":"airdropsdac5","memo":"*HireVibes (HVT) https://www.hirevibes.io/ Token Airdrop and Claim opportunity has started. For more info visit: https://goo.gl/nrELgi | To claim, use our website Claim Tool OR go to: https://goo.gl/em1x3o OR Transfer any amount of HVT to any account.*","timestamp":"2018-11-05T05:26:28.500","status":"executed"}]
             */

            private int trace_count;
            private List<TraceListBean> trace_list;

            public int getTrace_count() {
                return trace_count;
            }

            public void setTrace_count(int trace_count) {
                this.trace_count = trace_count;
            }

            public List<TraceListBean> getTrace_list() {
                return trace_list;
            }

            public void setTrace_list(List<TraceListBean> trace_list) {
                this.trace_list = trace_list;
            }

            public static class TraceListBean {
                /**
                 * trx_id : dfab30f059c8180d6aa04db5818486cc67daaa35a63b6d58d47a53eadb8be4f3
                 * symbol : HVT
                 * code : hirevibeshvt
                 * quantity : 0.8775
                 * receiver : yfhuangeos55
                 * sender : airdropsdac5
                 * memo : *HireVibes (HVT) https://www.hirevibes.io/ Token Airdrop and Claim opportunity has started. For more info visit: https://goo.gl/nrELgi | To claim, use our website Claim Tool OR go to: https://goo.gl/em1x3o OR Transfer any amount of HVT to any account.*
                 * timestamp : 2018-11-05T05:26:28.500
                 * status : executed
                 */

                private String trx_id;
                private String symbol;
                @SerializedName("code")
                private String codeX;
                private String quantity;
                private String receiver;
                private String sender;
                private String memo;
                private String timestamp;
                private String status;

                public String getTrx_id() {
                    return trx_id;
                }

                public void setTrx_id(String trx_id) {
                    this.trx_id = trx_id;
                }

                public String getSymbol() {
                    return symbol;
                }

                public void setSymbol(String symbol) {
                    this.symbol = symbol;
                }

                public String getCodeX() {
                    return codeX;
                }

                public void setCodeX(String codeX) {
                    this.codeX = codeX;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public String getReceiver() {
                    return receiver;
                }

                public void setReceiver(String receiver) {
                    this.receiver = receiver;
                }

                public String getSender() {
                    return sender;
                }

                public void setSender(String sender) {
                    this.sender = sender;
                }

                public String getMemo() {
                    return memo;
                }

                public void setMemo(String memo) {
                    this.memo = memo;
                }

                public String getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(String timestamp) {
                    this.timestamp = timestamp;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }
    }
}
