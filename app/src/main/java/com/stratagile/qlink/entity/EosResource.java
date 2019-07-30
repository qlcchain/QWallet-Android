package com.stratagile.qlink.entity;

public class EosResource extends BaseBack<EosResource.DataBeanX> {

    /**
     * data : {"errno":0,"data":{"staked":{"net_weight":"0.1343 EOS","cpu_weight":"3.2500 EOS"},"cpu":{"max":25060,"available":16741,"used":8319},"net":{"max":101291,"available":99698,"used":1593},"ram":{"available":10098,"used":4114}},"errmsg":"Success"}
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
         * data : {"staked":{"net_weight":"0.1343 EOS","cpu_weight":"3.2500 EOS"},"cpu":{"max":25060,"available":16741,"used":8319},"net":{"max":101291,"available":99698,"used":1593},"ram":{"available":10098,"used":4114}}
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
             * staked : {"net_weight":"0.1343 EOS","cpu_weight":"3.2500 EOS"}
             * cpu : {"max":25060,"available":16741,"used":8319}
             * net : {"max":101291,"available":99698,"used":1593}
             * ram : {"available":10098,"used":4114}
             */

            private StakedBean staked;
            private CpuBean cpu;
            private NetBean net;
            private RamBean ram;

            public StakedBean getStaked() {
                return staked;
            }

            public void setStaked(StakedBean staked) {
                this.staked = staked;
            }

            public CpuBean getCpu() {
                return cpu;
            }

            public void setCpu(CpuBean cpu) {
                this.cpu = cpu;
            }

            public NetBean getNet() {
                return net;
            }

            public void setNet(NetBean net) {
                this.net = net;
            }

            public RamBean getRam() {
                return ram;
            }

            public void setRam(RamBean ram) {
                this.ram = ram;
            }

            public static class StakedBean {
                /**
                 * net_weight : 0.1343 EOS
                 * cpu_weight : 3.2500 EOS
                 */

                private String net_weight;
                private String cpu_weight;

                public String getNet_weight() {
                    return net_weight;
                }

                public void setNet_weight(String net_weight) {
                    this.net_weight = net_weight;
                }

                public String getCpu_weight() {
                    return cpu_weight;
                }

                public void setCpu_weight(String cpu_weight) {
                    this.cpu_weight = cpu_weight;
                }
            }

            public static class CpuBean {
                /**
                 * max : 25060
                 * available : 16741
                 * used : 8319
                 */

                private int max;
                private int available;
                private int used;

                public int getMax() {
                    return max;
                }

                public void setMax(int max) {
                    this.max = max;
                }

                public int getAvailable() {
                    return available;
                }

                public void setAvailable(int available) {
                    this.available = available;
                }

                public int getUsed() {
                    return used;
                }

                public void setUsed(int used) {
                    this.used = used;
                }
            }

            public static class NetBean {
                /**
                 * max : 101291
                 * available : 99698
                 * used : 1593
                 */

                private int max;
                private int available;
                private int used;

                public int getMax() {
                    return max;
                }

                public void setMax(int max) {
                    this.max = max;
                }

                public int getAvailable() {
                    return available;
                }

                public void setAvailable(int available) {
                    this.available = available;
                }

                public int getUsed() {
                    return used;
                }

                public void setUsed(int used) {
                    this.used = used;
                }
            }

            public static class RamBean {
                /**
                 * available : 10098
                 * used : 4114
                 */

                private int available;
                private int used;

                public int getAvailable() {
                    return available;
                }

                public void setAvailable(int available) {
                    this.available = available;
                }

                public int getUsed() {
                    return used;
                }

                public void setUsed(int used) {
                    this.used = used;
                }
            }
        }
    }
}
