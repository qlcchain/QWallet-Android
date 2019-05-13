package com.stratagile.qlink.entity;

import java.util.ArrayList;
import java.util.List;

public class EthWalletInfo extends BaseBack<EthWalletInfo.DataBean> {

    /**
     * data : {"address":"0x7d5114d0eb75beaa24344a8f8adbdf1936525cc5","ETH":{"balance":0},"countTxs":23,"tokens":[{"tokenInfo":{"address":"0x7f686b69565522ed7fb05c2ee1406a3a0b45fcaa","name":"MyTokenC","decimals":"3","symbol":"YYC","totalSupply":"1000000000000","owner":"0xfdae196edc10a085d95cf157c658d526fb94e4ae","lastUpdated":1527143709,"issuancesCount":0,"holdersCount":8,"price":false},"balance":89991000,"totalIn":0,"totalOut":0},{"tokenInfo":{"address":"0x48f775efbe4f5ece6e0df2f7b5932df56823b990","name":"R token","decimals":"0","symbol":"R","totalSupply":"1000000000","owner":"0x","lastUpdated":1540547052,"issuancesCount":0,"holdersCount":53004,"ethTransfersCount":1,"price":{"rate":"0.2421342518","diff":17.31,"diff7d":55.03,"ts":"1540546538","marketCapUsd":"80981801.0","availableSupply":"334450000.0","volume24h":"3631741.74917","diff30d":130.76668,"currency":"USD"}},"balance":4,"totalIn":0,"totalOut":0},{"tokenInfo":{"address":"0x4022eb64d742d88f71070ba6a1fcbb5d11275a9f","name":"Payin Token","decimals":"18","symbol":"PAYIN","totalSupply":"1000000000000000000000000000","owner":"0x","lastUpdated":1535996749,"issuancesCount":0,"holdersCount":1648,"price":false},"balance":1.3734217E19,"totalIn":0,"totalOut":0}]}
     */

//    private DataBean data;
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }

    public static class DataBean {
        /**
         * address : 0x7d5114d0eb75beaa24344a8f8adbdf1936525cc5
         * ETH : {"balance":0}
         * countTxs : 23
         * tokens : [{"tokenInfo":{"address":"0x7f686b69565522ed7fb05c2ee1406a3a0b45fcaa","name":"MyTokenC","decimals":"3","symbol":"YYC","totalSupply":"1000000000000","owner":"0xfdae196edc10a085d95cf157c658d526fb94e4ae","lastUpdated":1527143709,"issuancesCount":0,"holdersCount":8,"price":false},"balance":89991000,"totalIn":0,"totalOut":0},{"tokenInfo":{"address":"0x48f775efbe4f5ece6e0df2f7b5932df56823b990","name":"R token","decimals":"0","symbol":"R","totalSupply":"1000000000","owner":"0x","lastUpdated":1540547052,"issuancesCount":0,"holdersCount":53004,"ethTransfersCount":1,"price":{"rate":"0.2421342518","diff":17.31,"diff7d":55.03,"ts":"1540546538","marketCapUsd":"80981801.0","availableSupply":"334450000.0","volume24h":"3631741.74917","diff30d":130.76668,"currency":"USD"}},"balance":4,"totalIn":0,"totalOut":0},{"tokenInfo":{"address":"0x4022eb64d742d88f71070ba6a1fcbb5d11275a9f","name":"Payin Token","decimals":"18","symbol":"PAYIN","totalSupply":"1000000000000000000000000000","owner":"0x","lastUpdated":1535996749,"issuancesCount":0,"holdersCount":1648,"price":false},"balance":1.3734217E19,"totalIn":0,"totalOut":0}]
         */

        private String address;
        private ETHBean ETH;
        private int countTxs;
        private ArrayList<TokensBean> tokens;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public ETHBean getETH() {
            return ETH;
        }

        public void setETH(ETHBean ETH) {
            this.ETH = ETH;
        }

        public int getCountTxs() {
            return countTxs;
        }

        public void setCountTxs(int countTxs) {
            this.countTxs = countTxs;
        }

        public ArrayList<TokensBean> getTokens() {
            return tokens;
        }

        public void setTokens(ArrayList<TokensBean> tokens) {
            this.tokens = tokens;
        }

        public static class ETHBean {
            /**
             * balance : 0
             */

            private Object balance;

            public Object getBalance() {
                return balance;
            }

            public void setBalance(Object balance) {
                this.balance = balance;
            }
        }

        public static class TokensBean {
            /**
             * tokenInfo : {"address":"0x7f686b69565522ed7fb05c2ee1406a3a0b45fcaa","name":"MyTokenC","decimals":"3","symbol":"YYC","totalSupply":"1000000000000","owner":"0xfdae196edc10a085d95cf157c658d526fb94e4ae","lastUpdated":1527143709,"issuancesCount":0,"holdersCount":8,"price":false}
             * balance : 89991000
             * totalIn : 0
             * totalOut : 0
             */

            private TokenInfoBean tokenInfo;
            private double balance;
            private int totalIn;
            private int totalOut;

            public TokenInfoBean getTokenInfo() {
                return tokenInfo;
            }

            public void setTokenInfo(TokenInfoBean tokenInfo) {
                this.tokenInfo = tokenInfo;
            }

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public int getTotalIn() {
                return totalIn;
            }

            public void setTotalIn(int totalIn) {
                this.totalIn = totalIn;
            }

            public int getTotalOut() {
                return totalOut;
            }

            public void setTotalOut(int totalOut) {
                this.totalOut = totalOut;
            }

            public static class TokenInfoBean {
                /**
                 * address : 0x7f686b69565522ed7fb05c2ee1406a3a0b45fcaa
                 * name : MyTokenC
                 * decimals : 3
                 * symbol : YYC
                 * totalSupply : 1000000000000
                 * owner : 0xfdae196edc10a085d95cf157c658d526fb94e4ae
                 * lastUpdated : 1527143709
                 * issuancesCount : 0
                 * holdersCount : 8
                 * price : false
                 */

                private String address;
                private String name;
                private String decimals;
                private String symbol;
                private String totalSupply;
                private String owner;
                private int lastUpdated;
                private int issuancesCount;
                private int holdersCount;
                private Object price;

                public Object getPrice() {
                    return price;
                }

                public void setPrice(Object price) {
                    this.price = price;
                }

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

                public static class priceBean {

                    /**
                     * rate : 0.2421342518
                     * diff : 17.31
                     * diff7d : 55.03
                     * ts : 1540546538
                     * marketCapUsd : 80981801.0
                     * availableSupply : 334450000.0
                     * volume24h : 3631741.74917
                     * diff30d : 130.76668
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
