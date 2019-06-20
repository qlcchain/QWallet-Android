package com.stratagile.qlink.entity.otc;

import com.stratagile.qlink.entity.BaseBack;

public class Passport extends BaseBack {

    /**
     * facePhoto : /data/dapp/head/212fc0dbd56c4405968d7d2e0f57e78a.jpg
     * holdingPhoto : /data/dapp/head/9a814639647f4d4d99f518d134eccf05.jpg
     */

    private String facePhoto;
    private String holdingPhoto;

    public String getFacePhoto() {
        return facePhoto;
    }

    public void setFacePhoto(String facePhoto) {
        this.facePhoto = facePhoto;
    }

    public String getHoldingPhoto() {
        return holdingPhoto;
    }

    public void setHoldingPhoto(String holdingPhoto) {
        this.holdingPhoto = holdingPhoto;
    }
}
