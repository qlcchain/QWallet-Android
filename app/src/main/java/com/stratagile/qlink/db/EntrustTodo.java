package com.stratagile.qlink.db;

import com.stratagile.qlink.application.AppConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Map;

@Entity
public class EntrustTodo {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String token;
    private String pairsId;
    private String type;
    private String unitPrice;
    private String totalAmount;
    private String minAmount;
    private String maxAmount;
    private String qgasAddress;
    private String usdtAddress;
    private String fromAddress;
    private String txid;
    @Generated(hash = 1748122015)
    public EntrustTodo(Long id, String account, String token, String pairsId,
            String type, String unitPrice, String totalAmount, String minAmount,
            String maxAmount, String qgasAddress, String usdtAddress,
            String fromAddress, String txid) {
        this.id = id;
        this.account = account;
        this.token = token;
        this.pairsId = pairsId;
        this.type = type;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.qgasAddress = qgasAddress;
        this.usdtAddress = usdtAddress;
        this.fromAddress = fromAddress;
        this.txid = txid;
    }
    @Generated(hash = 1948411311)
    public EntrustTodo() {
    }

    public static void createEntrustTodo(Map<String, String> map) {
        EntrustTodo entrustTodo = new EntrustTodo();
        entrustTodo.setAccount(map.get("account"));
        entrustTodo.setToken(map.get("token"));
        entrustTodo.setPairsId(map.get("pairsId"));
        entrustTodo.setType(map.get("type"));
        entrustTodo.setUnitPrice(map.get("unitPrice"));
        entrustTodo.setTotalAmount(map.get("totalAmount"));
        entrustTodo.setMinAmount(map.get("minAmount"));
        entrustTodo.setMaxAmount(map.get("maxAmount"));
        entrustTodo.setQgasAddress(map.get("qgasAddress"));
        entrustTodo.setUsdtAddress(map.get("usdtAddress"));
        entrustTodo.setFromAddress(map.get("fromAddress"));
        entrustTodo.setTxid(map.get("txid"));
        AppConfig.getInstance().getDaoSession().getEntrustTodoDao().insert(entrustTodo);
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
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getPairsId() {
        return this.pairsId;
    }
    public void setPairsId(String pairsId) {
        this.pairsId = pairsId;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUnitPrice() {
        return this.unitPrice;
    }
    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }
    public String getTotalAmount() {
        return this.totalAmount;
    }
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getMinAmount() {
        return this.minAmount;
    }
    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }
    public String getMaxAmount() {
        return this.maxAmount;
    }
    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }
    public String getQgasAddress() {
        return this.qgasAddress;
    }
    public void setQgasAddress(String qgasAddress) {
        this.qgasAddress = qgasAddress;
    }
    public String getUsdtAddress() {
        return this.usdtAddress;
    }
    public void setUsdtAddress(String usdtAddress) {
        this.usdtAddress = usdtAddress;
    }
    public String getFromAddress() {
        return this.fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    public String getTxid() {
        return this.txid;
    }
    public void setTxid(String txid) {
        this.txid = txid;
    }
}
