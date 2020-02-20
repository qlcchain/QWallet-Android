package com.stratagile.qlink.entity;

public class PhoneNumber {
    private int id;
    private String phoneNumber;
    private String imsi;

    public int getSimId() {
        return simId;
    }

    public void setSimId(int simId) {
        this.simId = simId;
    }

    private int simId;
    private String simNumber;
    private int shunxu;

    public PhoneNumber(int id, String phoneNumber, String imsi, int simId, String simNumber, int shunxu) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.imsi = imsi;
        this.simId = simId;
        this.simNumber = simNumber;
        this.shunxu = shunxu;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public int getShunxu() {
        return shunxu;
    }

    public void setShunxu(int shunxu) {
        this.shunxu = shunxu;
    }

    public String isSelect() {
        return simNumber;
    }

    public void setSelect(String simNumber) {
        this.simNumber = simNumber;
    }

    public PhoneNumber(int id, String phoneNumber, String imsi, int simId) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.imsi = imsi;
        this.simId = simId;
    }

    public PhoneNumber(int id, String phoneNumber, String imsi, int simId, String simNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.imsi = imsi;
        this.simId = simId;
        this.simNumber = simNumber;
    }

    public PhoneNumber(int id, String phoneNumber, String imsi) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.imsi = imsi;
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imsi='" + imsi + '\'' +
                ", simId=" + simId +
                ", simNumber=" + simNumber +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
}
