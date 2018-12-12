package com.stratagile.qlink.blockchain.bean;

public class UnDelegatebwBean {

    /**
     * from : testnetstake
     * receiver : testnetstake
     * unstake_net_quantity : 100.0000 EOS
     * unstake_cpu_quantity : 100.0000 EOS
     */

    private String from;
    private String receiver;
    private String unstake_net_quantity;
    private String unstake_cpu_quantity;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getUnstake_net_quantity() {
        return unstake_net_quantity;
    }

    public void setUnstake_net_quantity(String unstake_net_quantity) {
        this.unstake_net_quantity = unstake_net_quantity;
    }

    public String getUnstake_cpu_quantity() {
        return unstake_cpu_quantity;
    }

    public void setUnstake_cpu_quantity(String unstake_cpu_quantity) {
        this.unstake_cpu_quantity = unstake_cpu_quantity;
    }
}
