package com.stratagile.qlink.entity.defi;

public class KeyStateBean {

    public KeyStateBean(double relative_1d, double value, String name) {
        this.relative_1d = relative_1d;
        this.value = value;
        this.name = name;
    }

    /**
     * relative_1d : 3.51
     * value : 48081.801372262344
     */

    private double relative_1d;
    private double value;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRelative_1d() {
        return relative_1d;
    }

    public void setRelative_1d(double relative_1d) {
        this.relative_1d = relative_1d;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
