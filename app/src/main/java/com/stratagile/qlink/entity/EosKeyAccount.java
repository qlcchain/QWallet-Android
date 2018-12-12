package com.stratagile.qlink.entity;

import java.util.ArrayList;

public class EosKeyAccount {

    /**
     * _id : 5bf0dcef371f9dd26888f4af
     * account : yfhuangeos2g
     * permission : owner
     * public_key : EOS6zegAh68vCp5mkG1vcWZAD8FJz6UquoB6Y45h9vktfWLWoa7fN
     * createdAt : 2018-11-18T03:30:55.144Z
     */

    private String _id;
    private String account;
    private String permission;
    private String public_key;
    private String createdAt;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
