package com.stratagile.qlink.entity.reward;

import com.stratagile.qlink.entity.BaseBack;

public class InviteTotal extends BaseBack {

    public float getTotalTopupReward() {
        return totalTopupReward;
    }

    public void setTotalTopupReward(float totalTopupReward) {
        this.totalTopupReward = totalTopupReward;
    }

    /**
     * inviteTotal : 0
     */

    private String inviteTotal;
    private float totalTopupReward;

    public String getInviteTotal() {
        return inviteTotal;
    }

    public void setInviteTotal(String inviteTotal) {
        this.inviteTotal = inviteTotal;
    }
}
