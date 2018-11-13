package com.stratagile.qlink.entity;

public class CurrencyBean {
    private String name;
    private boolean isCheck;
    private String currencyImg;

    public String getCurrencyImg() {
        return currencyImg;
    }

    public void setCurrencyImg(String currencyImg) {
        this.currencyImg = currencyImg;
    }

    public CurrencyBean(String name, boolean isCheck) {
        this.name = name;
        this.isCheck = isCheck;
    }

    public CurrencyBean(String name, boolean isCheck, String currencyImg) {
        this.name = name;
        this.isCheck = isCheck;
        this.currencyImg = currencyImg;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
