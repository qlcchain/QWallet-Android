package com.stratagile.qlink.entity.reward;

import com.stratagile.qlink.entity.BaseBack;

public class ClaimQgas extends BaseBack {

    /**
     * status : SUCCESS
     */

    private String rewardAmount;

    public String getStatus() {
        return rewardAmount;
    }

    public void setStatus(String status) {
        this.rewardAmount = status;
    }
}
