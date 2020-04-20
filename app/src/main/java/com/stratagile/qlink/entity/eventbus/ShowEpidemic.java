package com.stratagile.qlink.entity.eventbus;

public class ShowEpidemic {
    private boolean isIntrupte;

    public ShowEpidemic(boolean isIntrupte) {
        this.isIntrupte = isIntrupte;
    }

    public boolean isIntrupte() {
        return isIntrupte;
    }

    public void setIntrupte(boolean intrupte) {
        isIntrupte = intrupte;
    }
}
