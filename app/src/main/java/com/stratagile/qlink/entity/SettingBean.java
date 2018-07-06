package com.stratagile.qlink.entity;

public class SettingBean {
    private String icon;
    private String name;
    private String onStr;
    private String offStr;

    public String getOnStr() {
        return onStr;
    }

    public void setOnStr(String onStr) {
        this.onStr = onStr;
    }

    public String getOffStr() {
        return offStr;
    }

    public void setOffStr(String offStr) {
        this.offStr = offStr;
    }

    public int getCheckMode() {
        return checkMode;
    }

    private boolean fingerprintUnLock = false;

    public boolean isFingerprintUnLock() {
        return fingerprintUnLock;
    }

    public void setFingerprintUnLock(boolean fingerprintUnLock) {
        this.fingerprintUnLock = fingerprintUnLock;
    }

    public SettingBean(String icon, String name, String desc, int isCheckMode) {
        this.icon = icon;
        this.name = name;
        this.desc = desc;
        this.checkMode = isCheckMode;
    }

    public String getIcon() {
        return icon;

    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int isCheckMode() {
        return checkMode;
    }

    public void setCheckMode(int checkMode) {
        this.checkMode = checkMode;
    }

    private String desc;
    // 0 为向右箭头，1为switchbutton，2为隐藏
    private int checkMode;
}
