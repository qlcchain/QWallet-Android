package com.stratagile.qlink.db;

import com.google.gson.Gson;
import com.stratagile.qlink.walletconnect.WCSession;
import com.stratagile.qlink.walletconnect.entity.WCPeerMeta;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DWCSession {
    @Id(autoincrement = true)
    private Long id;
    private String sessionId;
    private String peerId;          // Randomly chosen by us when we first start the session, should stay the same for subsequent connections
    private String sessionData;     // Session data, formed from the wc: connect code when we first start a session - it will contain the sessionId key
    private String remotePeerData;  // Peer data from the other end of the connection. This will contain the icon & URL for the connection information
    private String remotePeerId;    // This is the connection key we receive from the walletconnect server after we establish the first connection.
    //   When we reconnect this session, we use this value in the client.connect(...) call
    private int usageCount;         // How many times we used this session
    private long lastUsageTime;     // Last time we used this session
    private String walletAccount;   // Which wallet we connected this session with (note, you can add/remove available session wallets using the update API call,
    //   maybe add this as an advanced option).

    @Generated(hash = 1395176557)
    public DWCSession(Long id, String sessionId, String peerId, String sessionData, String remotePeerData, String remotePeerId, int usageCount, long lastUsageTime,
            String walletAccount) {
        this.id = id;
        this.sessionId = sessionId;
        this.peerId = peerId;
        this.sessionData = sessionData;
        this.remotePeerData = remotePeerData;
        this.remotePeerId = remotePeerId;
        this.usageCount = usageCount;
        this.lastUsageTime = lastUsageTime;
        this.walletAccount = walletAccount;
    }
    @Generated(hash = 366751034)
    public DWCSession() {
    }
    
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPeerId() {
        return this.peerId;
    }
    public void setPeerId(String peerId) {
        this.peerId = peerId;
    }
    public String getSessionData() {
        return this.sessionData;
    }
    public void setSessionData(String sessionData) {
        this.sessionData = sessionData;
    }
    public WCSession getSession()
    {
        return new Gson().fromJson(sessionData, WCSession.class);
    }
    public String getRemotePeerData()
    {
        return remotePeerData;
    }
    public WCPeerMeta getRemotePeerData1()
    {
        return new Gson().fromJson(remotePeerData, WCPeerMeta.class);
    }
    public void setRemotePeerData(String remotePeerData) {
        this.remotePeerData = remotePeerData;
    }
    public String getRemotePeerId() {
        return this.remotePeerId;
    }
    public void setRemotePeerId(String remotePeerId) {
        this.remotePeerId = remotePeerId;
    }
    public int getUsageCount() {
        return this.usageCount;
    }
    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }
    public long getLastUsageTime() {
        return this.lastUsageTime;
    }
    public void setLastUsageTime(long lastUsageTime) {
        this.lastUsageTime = lastUsageTime;
    }
    public String getWalletAccount() {
        return this.walletAccount;
    }
    public void setWalletAccount(String walletAccount) {
        this.walletAccount = walletAccount;
    }
    public String getSessionId() {
        return this.sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
}
