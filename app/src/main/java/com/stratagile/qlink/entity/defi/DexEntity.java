package com.stratagile.qlink.entity.defi;

public class DexEntity {

    /**
     * name : SakeSwap
     * url : https://sakeswap.finance/#/swap
     * img :
     */

    private String name;
    private String url;
    private String img;

    @Override
    public String toString() {
        return "DexEntity{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", img='" + img + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
