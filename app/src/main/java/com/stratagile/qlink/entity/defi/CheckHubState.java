package com.stratagile.qlink.entity.defi;

public class CheckHubState {


    /**
     * state : 17
     * stateStr : WithDrawNeoFetchDone
     * rHash : 8643d4d2178531be47b26fbe1ff6c8639cbc0cebf79e1134c0b8203b535cefa9
     * rOrigin :
     * amount : 1300000000
     * userErc20Addr : 255eEcd17E11C5d2FFD5818da31d04B5c1721D7C
     * userNep5Addr :
     * lockedNep5Hash : 1d582df3a5bf2ebe8eac70dde3715938c8772dc577854e51fd3bfd6d19564988
     * lockedNep5Height : 4774992
     * lockedErc20Hash : 0x94b78b9f6a456f4d3afb38c77dfe9756620f9ad66c8d07c91fa63985baf46adf
     * lockedErc20Height : 7129843
     * unlockedNep5Hash : c61f0da66720adc3a1d0c1500b58bec8f3c62c3e5fe96d0c59cc7f3fe19aaabb
     * unlockedNep5Height : 4775017
     * unlockedErc20Hash :
     * unlockedErc20Height : 0
     * startTime : 2020-09-03T03:26:12Z
     * lastModifyTime : 2020-09-03T06:18:57Z
     * neoTimeout : false
     * ethTimeout : true
     * fail : false
     * remark :
     */

    private String state;
    private String stateStr;
    private String rHash;
    private String rOrigin;
    private String amount;
    private String userErc20Addr;
    private String userNep5Addr;
    private String lockedNep5Hash;
    private int lockedNep5Height;
    private String lockedErc20Hash;
    private int lockedErc20Height;
    private String unlockedNep5Hash;
    private int unlockedNep5Height;
    private String unlockedErc20Hash;
    private int unlockedErc20Height;
    private String startTime;
    private String lastModifyTime;
    private boolean neoTimeout;
    private boolean ethTimeout;
    private boolean fail;
    private String remark;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getRHash() {
        return rHash;
    }

    public void setRHash(String rHash) {
        this.rHash = rHash;
    }

    public String getROrigin() {
        return rOrigin;
    }

    public void setROrigin(String rOrigin) {
        this.rOrigin = rOrigin;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserErc20Addr() {
        return userErc20Addr;
    }

    public void setUserErc20Addr(String userErc20Addr) {
        this.userErc20Addr = userErc20Addr;
    }

    public String getUserNep5Addr() {
        return userNep5Addr;
    }

    public void setUserNep5Addr(String userNep5Addr) {
        this.userNep5Addr = userNep5Addr;
    }

    public String getLockedNep5Hash() {
        return lockedNep5Hash;
    }

    public void setLockedNep5Hash(String lockedNep5Hash) {
        this.lockedNep5Hash = lockedNep5Hash;
    }

    public int getLockedNep5Height() {
        return lockedNep5Height;
    }

    public void setLockedNep5Height(int lockedNep5Height) {
        this.lockedNep5Height = lockedNep5Height;
    }

    public String getLockedErc20Hash() {
        return lockedErc20Hash;
    }

    public void setLockedErc20Hash(String lockedErc20Hash) {
        this.lockedErc20Hash = lockedErc20Hash;
    }

    public int getLockedErc20Height() {
        return lockedErc20Height;
    }

    public void setLockedErc20Height(int lockedErc20Height) {
        this.lockedErc20Height = lockedErc20Height;
    }

    public String getUnlockedNep5Hash() {
        return unlockedNep5Hash;
    }

    public void setUnlockedNep5Hash(String unlockedNep5Hash) {
        this.unlockedNep5Hash = unlockedNep5Hash;
    }

    public int getUnlockedNep5Height() {
        return unlockedNep5Height;
    }

    public void setUnlockedNep5Height(int unlockedNep5Height) {
        this.unlockedNep5Height = unlockedNep5Height;
    }

    public String getUnlockedErc20Hash() {
        return unlockedErc20Hash;
    }

    public void setUnlockedErc20Hash(String unlockedErc20Hash) {
        this.unlockedErc20Hash = unlockedErc20Hash;
    }

    public int getUnlockedErc20Height() {
        return unlockedErc20Height;
    }

    public void setUnlockedErc20Height(int unlockedErc20Height) {
        this.unlockedErc20Height = unlockedErc20Height;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public boolean isNeoTimeout() {
        return neoTimeout;
    }

    public void setNeoTimeout(boolean neoTimeout) {
        this.neoTimeout = neoTimeout;
    }

    public boolean isEthTimeout() {
        return ethTimeout;
    }

    public void setEthTimeout(boolean ethTimeout) {
        this.ethTimeout = ethTimeout;
    }

    public boolean isFail() {
        return fail;
    }

    public void setFail(boolean fail) {
        this.fail = fail;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
