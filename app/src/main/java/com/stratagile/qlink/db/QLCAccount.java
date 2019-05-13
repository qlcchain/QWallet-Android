package com.stratagile.qlink.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class QLCAccount {
    @Id(autoincrement = true)

    private Long id;

    private String seed;
    private String password;
    private String pubKey;
    private String privKey;
    private String address;
    private boolean isCurrent;
    private String accountName;

    @Override
    public String toString() {
        return "QLCAccount{" +
                "id=" + id +
                ", seed='" + seed + '\'' +
                ", password='" + password + '\'' +
                ", pubKey='" + pubKey + '\'' +
                ", privKey='" + privKey + '\'' +
                ", address='" + address + '\'' +
                ", isCurrent=" + isCurrent +
                ", accountName='" + accountName + '\'' +
                '}';
    }

    @Generated(hash = 502131092)
    public QLCAccount(Long id, String seed, String password, String pubKey,
            String privKey, String address, boolean isCurrent, String accountName) {
        this.id = id;
        this.seed = seed;
        this.password = password;
        this.pubKey = pubKey;
        this.privKey = privKey;
        this.address = address;
        this.isCurrent = isCurrent;
        this.accountName = accountName;
    }

    @Generated(hash = 1173276367)
    public QLCAccount() {
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSeed() {
        return this.seed;
    }
    public void setSeed(String seed) {
        this.seed = seed;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPubKey() {
        return this.pubKey;
    }
    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }
    public String getPrivKey() {
        return this.privKey;
    }
    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getIsCurrent() {
        return this.isCurrent;
    }

    public void setIsCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
