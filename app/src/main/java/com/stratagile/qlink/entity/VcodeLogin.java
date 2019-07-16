package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

public class VcodeLogin extends BaseBack<String> {


    /**
     * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
     * data : MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF4Udd/6e3wjiLrJQG/81NwfUlSLWygnALqoqLtAmCRoewfDZ+/a3B1o4xv9QFW61MIMa0SrmOUsLZXsUWDjWYVzuq1Joo9O4OwKJe6Tz+5kEzubendrttocuvLm/hrsqJ4iDgM37Wb7JhuNZfVWeNtk6GWgj18bAFu3FTthLCcwIDAQAB
     * phone : 18670819116
     * facePhoto : /data/dapp/head/58b574e39225445999677fc60be3490d.jpg
     * vstatus : UPLOADED
     * nickname :
     * id : 19528463
     * account : 18670819116
     * email :
     * holdingPhoto : /data/dapp/head/44ca676639dd499baaaa3b55c5035375.jpg
     */

    private String head;
    private String phone;
    private String facePhoto;
    private String vstatus;
    private String nickname;
    private String id;
    private String account;
    private String email;
    private String holdingPhoto;
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacePhoto() {
        return facePhoto;
    }

    public void setFacePhoto(String facePhoto) {
        this.facePhoto = facePhoto;
    }

    public String getVstatus() {
        return vstatus;
    }

    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoldingPhoto() {
        return holdingPhoto;
    }

    public void setHoldingPhoto(String holdingPhoto) {
        this.holdingPhoto = holdingPhoto;
    }
}
