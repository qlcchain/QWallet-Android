package com.stratagile.qlink.entity;

/**
 * Created by huzhipeng on 2018/1/26.
 */

public class Raw extends BaseBack<Raw.DataBean> {

    /**
     * data : {"rates":{"NEO":{"QLC":460.59},"GAS":{"QLC":164.5},"BNB":{"QLC":103.31}}}
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
         * rates : {"NEO":{"QLC":460.59},"GAS":{"QLC":164.5},"BNB":{"QLC":103.31}}
         */

        private RatesBean rates;

        public RatesBean getRates() {
            return rates;
        }

        public void setRates(RatesBean rates) {
            this.rates = rates;
        }

        public static class RatesBean {
            /**
             * NEO : {"QLC":460.59}
             * GAS : {"QLC":164.5}
             * BNB : {"QLC":103.31}
             */

            private NEOBean NEO;
            private GASBean GAS;
            private BNBBean BNB;

            public NEOBean getNEO() {
                return NEO;
            }

            public void setNEO(NEOBean NEO) {
                this.NEO = NEO;
            }

            public GASBean getGAS() {
                return GAS;
            }

            public void setGAS(GASBean GAS) {
                this.GAS = GAS;
            }

            public BNBBean getBNB() {
                return BNB;
            }

            public void setBNB(BNBBean BNB) {
                this.BNB = BNB;
            }

            public static class NEOBean {
                /**
                 * QLC : 460.59
                 */

                private double QLC;

                public double getQLC() {
                    return QLC;
                }

                public void setQLC(double QLC) {
                    this.QLC = QLC;
                }
            }

            public static class GASBean {
                /**
                 * QLC : 164.5
                 */

                private double QLC;

                public double getQLC() {
                    return QLC;
                }

                public void setQLC(double QLC) {
                    this.QLC = QLC;
                }
            }

            public static class BNBBean {
                /**
                 * QLC : 103.31
                 */

                private double QLC;

                public double getQLC() {
                    return QLC;
                }

                public void setQLC(double QLC) {
                    this.QLC = QLC;
                }
            }
        }
    }
}
