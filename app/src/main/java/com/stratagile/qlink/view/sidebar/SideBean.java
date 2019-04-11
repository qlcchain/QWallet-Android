package com.stratagile.qlink.view.sidebar;


public class SideBean{
    private String name;

    private String indexTag;

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public SideBean(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
