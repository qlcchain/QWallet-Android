package com.stratagile.qlink.entity.defi;


import com.stratagile.qlink.entity.BaseBack;

public class DefiDetail extends BaseBack {

    /**
     * project : {"chain":"Ethereum","website":"https://cdp.makerdao.com/","jsonValue":"{\"balance\":{\"ERC20\":{\"DAI\":{\"relative_1d\":-0.35,\"value\":11789524.084123181}}},\"total\":{\"BTC\":{\"relative_1d\":764.66,\"value\":1124.5020353},\"ETH\":{\"relative_1d\":0.49,\"value\":2001687.621657313},\"USD\":{\"relative_1d\":1.49,\"value\":458441705}},\"tvl\":{\"BTC\":{\"relative_1d\":3.51,\"value\":48081.801372262344},\"ETH\":{\"relative_1d\":2.77,\"value\":2180667.388098749},\"USD\":{\"relative_1d\":1.49,\"value\":458441705}}}","rating":"7","description":"MakerDAO is a decentralized credit platform on Ethereum that supports Dai, a stablecoin whose value is pegged to USD. Anyone can use Maker to open a Vault, lock in collateral such as ETH or BAT, and generate Dai as debt against that collateral. Dai debt incurs a stability fee (i.e., continuously accruing interest), which is paid upon repayment of borrowed Dai. That MKR is burned, along with the repaid Dai. Users can borrow Dai up to 66% of their collateral&rsquo;s value (150% collateralization ratio). Vaults that fall below that rate are subject to a 13% penalty and liquidation (by anyone) to bring the Vault out of default. Liquidated collateral is sold on an open market at a 3% discount.\r\n\r\nHolders of Maker&rsquo;s other token (MKR) govern the system by voting on, e.g., risk parameters such as the stability fee level. MKR holders also act as the last line of defense in case of a black swan event. If system-wide collateral value falls too low too fast, MKR is minted and sold on the open market to raise more collateral, diluting MKR holders.\r\n\r\nMaker also has a feature called the Dai Savings Rate (DSR). DAI holders can lock their DAI into Maker&rsquo;s DSR contract and earn a variable interest rate in DAI, which is generated from stability fees.","variability":"Medium","contributesTo":"","permissioning":"Open","name":"Maker","id":"087d49ad7512453a9b1f269ce0775a9d","shortName":"","category":"Lending","tvlUsd":"458441705.000000000000","relative":"1.49"}
     */

    private ProjectBean project;

    public ProjectBean getProject() {
        return project;
    }

    public void setProject(ProjectBean project) {
        this.project = project;
    }

    public static class ProjectBean {
        /**
         * chain : Ethereum
         * website : https://cdp.makerdao.com/
         * jsonValue : {"balance":{"ERC20":{"DAI":{"relative_1d":-0.35,"value":11789524.084123181}}},"total":{"BTC":{"relative_1d":764.66,"value":1124.5020353},"ETH":{"relative_1d":0.49,"value":2001687.621657313},"USD":{"relative_1d":1.49,"value":458441705}},"tvl":{"BTC":{"relative_1d":3.51,"value":48081.801372262344},"ETH":{"relative_1d":2.77,"value":2180667.388098749},"USD":{"relative_1d":1.49,"value":458441705}}}
         * rating : 7
         * description : MakerDAO is a decentralized credit platform on Ethereum that supports Dai, a stablecoin whose value is pegged to USD. Anyone can use Maker to open a Vault, lock in collateral such as ETH or BAT, and generate Dai as debt against that collateral. Dai debt incurs a stability fee (i.e., continuously accruing interest), which is paid upon repayment of borrowed Dai. That MKR is burned, along with the repaid Dai. Users can borrow Dai up to 66% of their collateral&rsquo;s value (150% collateralization ratio). Vaults that fall below that rate are subject to a 13% penalty and liquidation (by anyone) to bring the Vault out of default. Liquidated collateral is sold on an open market at a 3% discount.

         Holders of Maker&rsquo;s other token (MKR) govern the system by voting on, e.g., risk parameters such as the stability fee level. MKR holders also act as the last line of defense in case of a black swan event. If system-wide collateral value falls too low too fast, MKR is minted and sold on the open market to raise more collateral, diluting MKR holders.

         Maker also has a feature called the Dai Savings Rate (DSR). DAI holders can lock their DAI into Maker&rsquo;s DSR contract and earn a variable interest rate in DAI, which is generated from stability fees.
         * variability : Medium
         * contributesTo :
         * permissioning : Open
         * name : Maker
         * id : 087d49ad7512453a9b1f269ce0775a9d
         * shortName :
         * category : Lending
         * tvlUsd : 458441705.000000000000
         * relative : 1.49
         */

        private String chain;
        private String website;
        private String jsonValue;
        private String rating;
        private String description;
        private String variability;
        private String contributesTo;
        private String permissioning;
        private String name;
        private String id;
        private String shortName;
        private String category;
        private String tvlUsd;
        private String relative;

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getJsonValue() {
            return jsonValue;
        }

        public void setJsonValue(String jsonValue) {
            this.jsonValue = jsonValue;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVariability() {
            return variability;
        }

        public void setVariability(String variability) {
            this.variability = variability;
        }

        public String getContributesTo() {
            return contributesTo;
        }

        public void setContributesTo(String contributesTo) {
            this.contributesTo = contributesTo;
        }

        public String getPermissioning() {
            return permissioning;
        }

        public void setPermissioning(String permissioning) {
            this.permissioning = permissioning;
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

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTvlUsd() {
            return tvlUsd;
        }

        public void setTvlUsd(String tvlUsd) {
            this.tvlUsd = tvlUsd;
        }

        public String getRelative() {
            return relative;
        }

        public void setRelative(String relative) {
            this.relative = relative;
        }
    }
}
