package com.stratagile.qlink.db;

import com.stratagile.qlink.application.AppConfig;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Map;

@Entity
public class BuySellBuyTodo {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String token;
    private String tradeOrderId;
    private String txid;
    @Generated(hash = 1843037130)
    public BuySellBuyTodo(Long id, String account, String token,
            String tradeOrderId, String txid) {
        this.id = id;
        this.account = account;
        this.token = token;
        this.tradeOrderId = tradeOrderId;
        this.txid = txid;
    }

    public static void createBuySellBuyTodo(Map<String, String> map) {
        BuySellBuyTodo buySellBuyTodo = new BuySellBuyTodo();
        buySellBuyTodo.setAccount(map.get("account"));
        buySellBuyTodo.setToken(map.get("token"));
        buySellBuyTodo.setTradeOrderId(map.get("tradeOrderId"));
        buySellBuyTodo.setTxid(map.get("txid"));
        AppConfig.getInstance().getDaoSession().getBuySellBuyTodoDao().insert(buySellBuyTodo);
    }

    @Generated(hash = 811410100)
    public BuySellBuyTodo() {
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
    public String getTradeOrderId() {
        return this.tradeOrderId;
    }
    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }
    public String getTxid() {
        return this.txid;
    }
    public void setTxid(String txid) {
        this.txid = txid;
    }
}
