package com.stratagile.qlink.db;

import com.stratagile.qlink.application.AppConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Map;

@Entity
public class BuySellSellTodo {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String token;
    private String entrustOrderId;
    private String usdtAmount;
    private String usdtToAddress;
    private String qgasAmount;
    private String fromAddress;
    private String txid;
    @Generated(hash = 1627591181)
    public BuySellSellTodo(Long id, String account, String token,
            String entrustOrderId, String usdtAmount, String usdtToAddress,
            String qgasAmount, String fromAddress, String txid) {
        this.id = id;
        this.account = account;
        this.token = token;
        this.entrustOrderId = entrustOrderId;
        this.usdtAmount = usdtAmount;
        this.usdtToAddress = usdtToAddress;
        this.qgasAmount = qgasAmount;
        this.fromAddress = fromAddress;
        this.txid = txid;
    }
    @Generated(hash = 933435570)
    public BuySellSellTodo() {
    }

    public static void createBuySellSellTodo(Map<String, String> map) {
        BuySellSellTodo buySellSellTodo = new BuySellSellTodo();
        buySellSellTodo.setAccount(map.get("account"));
        buySellSellTodo.setToken(map.get("token"));
        buySellSellTodo.setEntrustOrderId(map.get("tradeOrderId"));
        buySellSellTodo.setTxid(map.get("txid"));
        AppConfig.getInstance().getDaoSession().getBuySellSellTodoDao().insert(buySellSellTodo);
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
    public String getEntrustOrderId() {
        return this.entrustOrderId;
    }
    public void setEntrustOrderId(String entrustOrderId) {
        this.entrustOrderId = entrustOrderId;
    }
    public String getUsdtAmount() {
        return this.usdtAmount;
    }
    public void setUsdtAmount(String usdtAmount) {
        this.usdtAmount = usdtAmount;
    }
    public String getUsdtToAddress() {
        return this.usdtToAddress;
    }
    public void setUsdtToAddress(String usdtToAddress) {
        this.usdtToAddress = usdtToAddress;
    }
    public String getQgasAmount() {
        return this.qgasAmount;
    }
    public void setQgasAmount(String qgasAmount) {
        this.qgasAmount = qgasAmount;
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
