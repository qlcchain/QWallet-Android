package com.stratagile.qlink.entity;

import java.util.List;

public class EthWalletTransaction extends BaseBack<String> {

    /**
     * data : {"operations":[{"timestamp":1529963627,"transactionHash":"0xe9bbf27ef03d207392ddfbfc14e28893591ad76d7feea21b6c7ef06ef8e9b3bc","tokenInfo":{"address":"0x48f775efbe4f5ece6e0df2f7b5932df56823b990","name":"R token","decimals":"0","symbol":"R","totalSupply":"1000000000","owner":"0x","txsCount":90988,"transfersCount":92204,"lastUpdated":1540804255,"issuancesCount":0,"holdersCount":52967,"ethTransfersCount":1,"price":{"rate":"0.2472102166","diff":0.27,"diff7d":47.74,"ts":"1540805202","marketCapUsd":"82679457.0","availableSupply":"334450000.0","volume24h":"2435993.20803","diff30d":137.05252,"currency":"USD"}},"type":"transfer","value":"4","from":"0x75e2ecbe0d8b83f4bb30bf8ab0f7e3cfa9271429","to":"0x7d5114d0eb75beaa24344a8f8adbdf1936525cc5"}]}
     */

//    private String data;
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }

    public static class EthTransactionBean {

        private List<OperationsBean> operations;

        public List<OperationsBean> getOperations() {
            return operations;
        }

        public void setOperations(List<OperationsBean> operations) {
            this.operations = operations;
        }

        public static class OperationsBean {
            /**
             * timestamp : 1529963627
             * transactionHash : 0xe9bbf27ef03d207392ddfbfc14e28893591ad76d7feea21b6c7ef06ef8e9b3bc
             * tokenInfo : {"address":"0x48f775efbe4f5ece6e0df2f7b5932df56823b990","name":"R token","decimals":"0","symbol":"R","totalSupply":"1000000000","owner":"0x","txsCount":90988,"transfersCount":92204,"lastUpdated":1540804255,"issuancesCount":0,"holdersCount":52967,"ethTransfersCount":1,"price":{"rate":"0.2472102166","diff":0.27,"diff7d":47.74,"ts":"1540805202","marketCapUsd":"82679457.0","availableSupply":"334450000.0","volume24h":"2435993.20803","diff30d":137.05252,"currency":"USD"}}
             * type : transfer
             * value : 4
             * from : 0x75e2ecbe0d8b83f4bb30bf8ab0f7e3cfa9271429
             * to : 0x7d5114d0eb75beaa24344a8f8adbdf1936525cc5
             */

            private int timestamp;
            private String transactionHash;
            private TokenInfoBean tokenInfo;
            private String type;
            private String value;
            private String from;
            private String to;

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

            public String getTransactionHash() {
                return transactionHash;
            }

            public void setTransactionHash(String transactionHash) {
                this.transactionHash = transactionHash;
            }

            public TokenInfoBean getTokenInfo() {
                return tokenInfo;
            }

            public void setTokenInfo(TokenInfoBean tokenInfo) {
                this.tokenInfo = tokenInfo;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
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

            public static class TokenInfoBean {
                /**
                 * address : 0x48f775efbe4f5ece6e0df2f7b5932df56823b990
                 * name : R token
                 * decimals : 0
                 * symbol : R
                 * totalSupply : 1000000000
                 * owner : 0x
                 * txsCount : 90988
                 * transfersCount : 92204
                 * lastUpdated : 1540804255
                 * issuancesCount : 0
                 * holdersCount : 52967
                 * ethTransfersCount : 1
                 * price : {"rate":"0.2472102166","diff":0.27,"diff7d":47.74,"ts":"1540805202","marketCapUsd":"82679457.0","availableSupply":"334450000.0","volume24h":"2435993.20803","diff30d":137.05252,"currency":"USD"}
                 */

                private String address;
                private String name;
                private String decimals;
                private String symbol;
                private String totalSupply;
                private String owner;
                private int txsCount;
                private int transfersCount;
                private int lastUpdated;
                private int issuancesCount;
                private int holdersCount;
                private int ethTransfersCount;
                private Object price;

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getDecimals() {
                    return decimals;
                }

                public void setDecimals(String decimals) {
                    this.decimals = decimals;
                }

                public String getSymbol() {
                    return symbol;
                }

                public void setSymbol(String symbol) {
                    this.symbol = symbol;
                }

                public String getTotalSupply() {
                    return totalSupply;
                }

                public void setTotalSupply(String totalSupply) {
                    this.totalSupply = totalSupply;
                }

                public String getOwner() {
                    return owner;
                }

                public void setOwner(String owner) {
                    this.owner = owner;
                }

                public int getTxsCount() {
                    return txsCount;
                }

                public void setTxsCount(int txsCount) {
                    this.txsCount = txsCount;
                }

                public int getTransfersCount() {
                    return transfersCount;
                }

                public void setTransfersCount(int transfersCount) {
                    this.transfersCount = transfersCount;
                }

                public int getLastUpdated() {
                    return lastUpdated;
                }

                public void setLastUpdated(int lastUpdated) {
                    this.lastUpdated = lastUpdated;
                }

                public int getIssuancesCount() {
                    return issuancesCount;
                }

                public void setIssuancesCount(int issuancesCount) {
                    this.issuancesCount = issuancesCount;
                }

                public int getHoldersCount() {
                    return holdersCount;
                }

                public void setHoldersCount(int holdersCount) {
                    this.holdersCount = holdersCount;
                }

                public int getEthTransfersCount() {
                    return ethTransfersCount;
                }

                public void setEthTransfersCount(int ethTransfersCount) {
                    this.ethTransfersCount = ethTransfersCount;
                }

                public Object getPrice() {
                    return price;
                }

                public void setPrice(Object price) {
                    this.price = price;
                }

                public static class PriceBean {
                    /**
                     * rate : 0.2472102166
                     * diff : 0.27
                     * diff7d : 47.74
                     * ts : 1540805202
                     * marketCapUsd : 82679457.0
                     * availableSupply : 334450000.0
                     * volume24h : 2435993.20803
                     * diff30d : 137.05252
                     * currency : USD
                     */

                    private String rate;
                    private double diff;
                    private double diff7d;
                    private String ts;
                    private String marketCapUsd;
                    private String availableSupply;
                    private String volume24h;
                    private double diff30d;
                    private String currency;

                    public String getRate() {
                        return rate;
                    }

                    public void setRate(String rate) {
                        this.rate = rate;
                    }

                    public double getDiff() {
                        return diff;
                    }

                    public void setDiff(double diff) {
                        this.diff = diff;
                    }

                    public double getDiff7d() {
                        return diff7d;
                    }

                    public void setDiff7d(double diff7d) {
                        this.diff7d = diff7d;
                    }

                    public String getTs() {
                        return ts;
                    }

                    public void setTs(String ts) {
                        this.ts = ts;
                    }

                    public String getMarketCapUsd() {
                        return marketCapUsd;
                    }

                    public void setMarketCapUsd(String marketCapUsd) {
                        this.marketCapUsd = marketCapUsd;
                    }

                    public String getAvailableSupply() {
                        return availableSupply;
                    }

                    public void setAvailableSupply(String availableSupply) {
                        this.availableSupply = availableSupply;
                    }

                    public String getVolume24h() {
                        return volume24h;
                    }

                    public void setVolume24h(String volume24h) {
                        this.volume24h = volume24h;
                    }

                    public double getDiff30d() {
                        return diff30d;
                    }

                    public void setDiff30d(double diff30d) {
                        this.diff30d = diff30d;
                    }

                    public String getCurrency() {
                        return currency;
                    }

                    public void setCurrency(String currency) {
                        this.currency = currency;
                    }
                }
            }
        }
    }
}
