package com.stratagile.qlink.entity.swap;

public class WrapperOnline {

    /**
     * ethContract : 0x9a36F711133188EDb3952b3A6ee29c6a3d2e3836
     * ethAddress : 0x0A8EFAacbeC7763855b9A39845DDbd03b03775C1
     * neoContract : 0533290f35572cd06e3667653255ffd6ee6430fb
     * neoAddress : ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv
     */

    private String ethContract;
    private String ethAddress;
    private String neoContract;
    private String neoAddress;

    @Override
    public String toString() {
        return "WrapperOnline{" +
                "ethContract='" + ethContract + '\'' +
                ", ethAddress='" + ethAddress + '\'' +
                ", neoContract='" + neoContract + '\'' +
                ", neoAddress='" + neoAddress + '\'' +
                '}';
    }

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
}
