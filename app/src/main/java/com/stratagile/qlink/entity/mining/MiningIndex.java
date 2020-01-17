package com.stratagile.qlink.entity.mining;

import com.stratagile.qlink.entity.BaseBack;

import java.util.ArrayList;
import java.util.List;

public class MiningIndex extends BaseBack {

    /**
     * totalRewardAmount : 100000.0
     * awardedTotal : 0
     * rewardRankings : [{"head":"/data/dapp/head/bafbc90f5a9c47fe80f92367897b4e8d.png","sequence":1,"totalReward":1.5776E-4,"name":"sketch","id":"8155b81f47384b788d8e015ea7cace4e"},{"head":"","sequence":2,"totalReward":3.1556E-4,"name":"indra.firm03@gmail.com","id":"284929d695f04fd7a6da9c2a5bc1ac4a"},{"head":"","sequence":3,"totalReward":4.7336E-4,"name":"minerg1524@gmail.com","id":"1bbb2692decd48d0b582c9cdff7f0ca8"},{"head":"","sequence":4,"totalReward":4.7336E-4,"name":"muplihun50@gmail.com","id":"a659c58e4f2b4506884a74223cda1de8"},{"head":"/data/dapp/head/80ac56835b8545ba8e021d147a090923.jpg","sequence":5,"totalReward":5.5228E-4,"name":"Teito","id":"4627e7a8cc87488195d090f5f1e27a03"},{"head":"","sequence":6,"totalReward":6.3116E-4,"name":"pecanducrypto@gmail.com","id":"a5054b8cc4544a6e8fd3541d0937bd1d"},{"head":"/data/dapp/head/8cd5be78d52347c3a4c955b243851357.png","sequence":7,"totalReward":7.1008E-4,"name":"crypto gabi","id":"d4216308cedd49faa80aa02f8c299004"},{"head":"/data/dapp/head/e9f9e605d5964f4b8330caeee0ff7c13.jpg","sequence":8,"totalReward":7.8896E-4,"name":"6899680","id":"864dd51d5f874152a1f89a9e73384176"},{"head":"/data/dapp/head/a8d08590d6e64f37983faf68c7593320.png","sequence":9,"totalReward":7.8896E-4,"name":"Jelly","id":"89d3b950953044ec8314ec0f15722110"},{"head":"","sequence":10,"totalReward":7.8896E-4,"name":"thebabirusa@protonmail.com","id":"ab3a590047844b4ea56e711e00ccf350"}]
     * totalFinish : 1.0
     * noAwardTotal : 0.78977472
     */

    private double totalRewardAmount;
    private double awardedTotal;
    private double totalFinish;
    private double noAwardTotal;
    private ArrayList<RewardRankingsBean> rewardRankings;

    public double getTotalRewardAmount() {
        return totalRewardAmount;
    }

    public void setTotalRewardAmount(double totalRewardAmount) {
        this.totalRewardAmount = totalRewardAmount;
    }

    public double getAwardedTotal() {
        return awardedTotal;
    }

    public void setAwardedTotal(double awardedTotal) {
        this.awardedTotal = awardedTotal;
    }

    public double getTotalFinish() {
        return totalFinish;
    }

    public void setTotalFinish(double totalFinish) {
        this.totalFinish = totalFinish;
    }

    public double getNoAwardTotal() {
        return noAwardTotal;
    }

    public void setNoAwardTotal(double noAwardTotal) {
        this.noAwardTotal = noAwardTotal;
    }

    public ArrayList<RewardRankingsBean> getRewardRankings() {
        return rewardRankings;
    }

    public void setRewardRankings(ArrayList<RewardRankingsBean> rewardRankings) {
        this.rewardRankings = rewardRankings;
    }

    public static class RewardRankingsBean {
        /**
         * head : /data/dapp/head/bafbc90f5a9c47fe80f92367897b4e8d.png
         * sequence : 1
         * totalReward : 1.5776E-4
         * name : sketch
         * id : 8155b81f47384b788d8e015ea7cace4e
         */

        private String head;
        private int sequence;
        private double totalReward;
        private String name;
        private String id;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public double getTotalReward() {
            return totalReward;
        }

        public void setTotalReward(double totalReward) {
            this.totalReward = totalReward;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
