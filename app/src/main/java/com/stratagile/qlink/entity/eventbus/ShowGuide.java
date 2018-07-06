package com.stratagile.qlink.entity.eventbus;

public class ShowGuide {
    /**
     * 0 代表 vpnRegister
     * 1 代表 VpnList选中一个item
     * 2 代表 选择国家
     * 3 代表 跳到vpn列表
     */
    private int number;

    public ShowGuide(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
