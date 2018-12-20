package com.stratagile.qlink.blockchain.bean;

import io.eblock.eos4j.ese.Action;
import io.eblock.eos4j.ese.DataType;

public class StakeCpuAndNet {
    private String from;
    private String receiver;
    private String stake_net_quantity;

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

    public String getStake_net_quantity() {
        return stake_net_quantity;
    }

    public void setStake_net_quantity(String stake_net_quantity) {
        this.stake_net_quantity = stake_net_quantity;
    }

    public String getStake_cpu_quantity() {
        return stake_cpu_quantity;
    }

    public void setStake_cpu_quantity(String stake_cpu_quantity) {
        this.stake_cpu_quantity = stake_cpu_quantity;
    }

    public long getTransfer() {
        return transfer;
    }

    public void setTransfer(long transfer) {
        this.transfer = transfer;
    }

    private String stake_cpu_quantity;
    private long transfer;
}
