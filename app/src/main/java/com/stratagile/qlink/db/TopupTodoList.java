package com.stratagile.qlink.db;

import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.entity.topup.TopupOrderList;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Map;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TopupTodoList {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String p2pId;
    private String productId;
    private String areaCode;
    private String phoneNumber;
    private String amount;
    private String txid;
    private String payTokenId;
    private boolean created;

    @Generated(hash = 1671334175)
    public TopupTodoList(Long id, String account, String p2pId, String productId,
            String areaCode, String phoneNumber, String amount, String txid,
            String payTokenId, boolean created) {
        this.id = id;
        this.account = account;
        this.p2pId = p2pId;
        this.productId = productId;
        this.areaCode = areaCode;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
        this.txid = txid;
        this.payTokenId = payTokenId;
        this.created = created;
    }

    @Generated(hash = 506690585)
    public TopupTodoList() {
    }

    public static void createTodoList(Map<String, String> map) {
        TopupTodoList topupTodoList = new TopupTodoList();
        if (map.get("account") != null) {
            topupTodoList.setAccount(map.get("account"));
        }
        topupTodoList.setP2pId(map.get("p2pId"));
        topupTodoList.setProductId(map.get("productId"));
        topupTodoList.setAreaCode(map.get("areaCode"));
        topupTodoList.setPhoneNumber(map.get("phoneNumber"));
        topupTodoList.setAmount(map.get("amount"));
        topupTodoList.setTxid(map.get("txid"));
        topupTodoList.setPayTokenId(map.get("payTokenId"));
        topupTodoList.setCreated(false);
        AppConfig.getInstance().getDaoSession().getTopupTodoListDao().insert(topupTodoList);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getP2pId() {
        return this.p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAreaCode() {
        return this.areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxid() {
        return this.txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getPayTokenId() {
        return this.payTokenId;
    }

    public void setPayTokenId(String payTokenId) {
        this.payTokenId = payTokenId;
    }

    public boolean getCreated() {
        return this.created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
