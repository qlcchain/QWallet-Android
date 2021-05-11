package com.stratagile.qlink.entity.defi;

import java.math.BigDecimal;

public class DefiJson {

    /**
     * balance : {"ERC20":{"DAI":{"relative_1d":-0.35,"value":1.1789524084123181E7}}}
     * total : {"BTC":{"relative_1d":764.66,"value":1124.5020353},"ETH":{"relative_1d":0.49,"value":2001687.621657313},"USD":{"relative_1d":1.49,"value":458441705}}
     * tvl : {"BTC":{"relative_1d":3.51,"value":48081.801372262344},"ETH":{"relative_1d":2.77,"value":2180667.388098749},"USD":{"relative_1d":1.49,"value":458441705}}
     */

    private BalanceBean balance;
    private TotalBean total;
    private TvlBean tvl;

    public BalanceBean getBalance() {
        return balance;
    }

    public void setBalance(BalanceBean balance) {
        this.balance = balance;
    }

    public TotalBean getTotal() {
        return total;
    }

    public void setTotal(TotalBean total) {
        this.total = total;
    }

    public TvlBean getTvl() {
        return tvl;
    }

    public void setTvl(TvlBean tvl) {
        this.tvl = tvl;
    }

    public static class BalanceBean {
        /**
         * ERC20 : {"DAI":{"relative_1d":-0.35,"value":1.1789524084123181E7}}
         */

        private ERC20Bean ERC20;

        public ERC20Bean getERC20() {
            return ERC20;
        }

        public void setERC20(ERC20Bean ERC20) {
            this.ERC20 = ERC20;
        }

        public static class ERC20Bean {
            /**
             * DAI : {"relative_1d":-0.35,"value":1.1789524084123181E7}
             */

            private DAIBean DAI;

            public DAIBean getDAI() {
                return DAI;
            }

            public void setDAI(DAIBean DAI) {
                this.DAI = DAI;
            }

            public static class DAIBean {
                /**
                 * relative_1d : -0.35
                 * value : 1.1789524084123181E7
                 */

                private double relative_1d;
                private double value;

                public double getRelative_1d() {
                    return relative_1d;
                }

                public void setRelative_1d(double relative_1d) {
                    this.relative_1d = relative_1d;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }
        }
    }

    public static class TotalBean {
        /**
         * BTC : {"relative_1d":764.66,"value":1124.5020353}
         * ETH : {"relative_1d":0.49,"value":2001687.621657313}
         * USD : {"relative_1d":1.49,"value":458441705}
         */

        private BTCBean BTC;
        private ETHBean ETH;
        private USDBean USD;

        public BTCBean getBTC() {
            return BTC;
        }

        public void setBTC(BTCBean BTC) {
            this.BTC = BTC;
        }

        public ETHBean getETH() {
            return ETH;
        }

        public void setETH(ETHBean ETH) {
            this.ETH = ETH;
        }

        public USDBean getUSD() {
            return USD;
        }

        public void setUSD(USDBean USD) {
            this.USD = USD;
        }

        public static class BTCBean {
            /**
             * relative_1d : 764.66
             * value : 1124.5020353
             */

            private double relative_1d;
            private double value;

            public double getRelative_1d() {
                return relative_1d;
            }

            public void setRelative_1d(double relative_1d) {
                this.relative_1d = relative_1d;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class ETHBean {
            /**
             * relative_1d : 0.49
             * value : 2001687.621657313
             */

            private double relative_1d;
            private double value;

            public double getRelative_1d() {
                return relative_1d;
            }

            public void setRelative_1d(double relative_1d) {
                this.relative_1d = relative_1d;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class USDBean {
            /**
             * relative_1d : 1.49
             * value : 458441705
             */

            private double relative_1d;
            private BigDecimal value;

            public double getRelative_1d() {
                return relative_1d;
            }

            public void setRelative_1d(double relative_1d) {
                this.relative_1d = relative_1d;
            }

            public BigDecimal getValue() {
                return value;
            }

            public void setValue(BigDecimal value) {
                this.value = value;
            }
        }
    }

    public static class TvlBean {
        /**
         * BTC : {"relative_1d":3.51,"value":48081.801372262344}
         * ETH : {"relative_1d":2.77,"value":2180667.388098749}
         * USD : {"relative_1d":1.49,"value":458441705}
         */

        private BTCBeanX BTC;
        private ETHBeanX ETH;
        private USDBeanX USD;

        public BTCBeanX getBTC() {
            return BTC;
        }

        public void setBTC(BTCBeanX BTC) {
            this.BTC = BTC;
        }

        public ETHBeanX getETH() {
            return ETH;
        }

        public void setETH(ETHBeanX ETH) {
            this.ETH = ETH;
        }

        public USDBeanX getUSD() {
            return USD;
        }

        public void setUSD(USDBeanX USD) {
            this.USD = USD;
        }

        public static class BTCBeanX {
            /**
             * relative_1d : 3.51
             * value : 48081.801372262344
             */

            private double relative_1d;
            private double value;

            public double getRelative_1d() {
                return relative_1d;
            }

            public void setRelative_1d(double relative_1d) {
                this.relative_1d = relative_1d;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class ETHBeanX {
            /**
             * relative_1d : 2.77
             * value : 2180667.388098749
             */

            private double relative_1d;
            private double value;

            public double getRelative_1d() {
                return relative_1d;
            }

            public void setRelative_1d(double relative_1d) {
                this.relative_1d = relative_1d;
            }

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }
        }

        public static class USDBeanX {
            /**
             * relative_1d : 1.49
             * value : 458441705
             */

            private double relative_1d;
            private BigDecimal value;

            public double getRelative_1d() {
                return relative_1d;
            }

            public void setRelative_1d(double relative_1d) {
                this.relative_1d = relative_1d;
            }

            public BigDecimal getValue() {
                return value;
            }

            public void setValue(BigDecimal value) {
                this.value = value;
            }
        }
    }
}
