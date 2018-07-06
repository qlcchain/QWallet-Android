package com.stratagile.qlink.entity.eventbus;

/**
 * Created by huzhipeng on 2018/3/14.
 */

public class SelectCountry {
    private String country;
    private String continent;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public SelectCountry(String country, String continent) {
        this.country = country;
        this.continent = continent;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
}
