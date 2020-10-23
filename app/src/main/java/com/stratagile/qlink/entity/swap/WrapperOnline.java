package com.stratagile.qlink.entity.swap;

public class WrapperOnline {


    @Override
    public String toString() {
        return "WrapperOnline{" +
                "ethContract='" + ethContract + '\'' +
                ", ethAddress='" + ethAddress + '\'' +
                ", neoContract='" + neoContract + '\'' +
                ", neoAddress='" + neoAddress + '\'' +
                ", ethBalance=" + ethBalance +
                ", neoBalance=" + neoBalance +
                ", withdrawLimit=" + withdrawLimit +
                ", minDepositAmount='" + minDepositAmount + '\'' +
                ", minWithdrawAmount='" + minWithdrawAmount + '\'' +
                '}';
    }

    /**
     * ethContract : 0x16e502c867C2d4CAC0F4B4dBd39AB722F5cEc050
     * ethAddress : 0x0A8EFAacbeC7763855b9A39845DDbd03b03775C1
     * neoContract : cedfd8f78bf46d28ac07b8e40b911199bd51951f
     * neoAddress : ANFnCg69c8VfE36hBhLZRrmofZ9CZU1vqZ
     * ethBalance : 2.1898026
     * neoBalance : 172.4
     * withdrawLimit : false
     * minDepositAmount : 100000000
     * minWithdrawAmount : 100000000
     */

    private String ethContract;
    private String ethAddress;
    private String neoContract;
    private String neoAddress;
    private double ethBalance;
    private double neoBalance;
    private boolean withdrawLimit;
    private String minDepositAmount;
    private String minWithdrawAmount;

    public String getEthContract() {
        return ethContract;
    }

    public void setEthContract(String ethContract) {
        this.ethContract = ethContract;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public String getNeoContract() {
        return neoContract;
    }

    public void setNeoContract(String neoContract) {
        this.neoContract = neoContract;
    }

    public String getNeoAddress() {
        return neoAddress;
    }

    public void setNeoAddress(String neoAddress) {
        this.neoAddress = neoAddress;
    }

    public double getEthBalance() {
        return ethBalance;
    }

    public void setEthBalance(double ethBalance) {
        this.ethBalance = ethBalance;
    }

    public double getNeoBalance() {
        return neoBalance;
    }

    public void setNeoBalance(double neoBalance) {
        this.neoBalance = neoBalance;
    }

    public boolean isWithdrawLimit() {
        return withdrawLimit;
    }

    public void setWithdrawLimit(boolean withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }

    public String getMinDepositAmount() {
        return minDepositAmount;
    }

    public void setMinDepositAmount(String minDepositAmount) {
        this.minDepositAmount = minDepositAmount;
    }

    public String getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(String minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }
}
