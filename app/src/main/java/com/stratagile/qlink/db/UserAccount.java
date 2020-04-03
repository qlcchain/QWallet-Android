package com.stratagile.qlink.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class UserAccount {
    @Id(autoincrement = true)
    private Long id;
    //账号名，只支持邮箱
    private String account;
    //用户密码
    private String password;
    //用户公钥
    private String pubKey;
    //是否登录
    private boolean isLogin;
    //邀请码
    private String inviteCode;
    //用户名
    private String userName;
    //用户头像路径
    private String avatar;

    private String test;

    private String bindDate;

    public int getTotalInvite() {
        return totalInvite;
    }

    public void setTotalInvite(int totalInvite) {
        this.totalInvite = totalInvite;
    }

    //手机号码
    private String phone;

    private String userId;

    private String email;

    //kyc状态
    private String vstatus;

    //正面照
    private String facePhoto;

    private int totalInvite;

    private String qlcAddress;

    //打开app自动登录的
    private boolean startApp;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getQlcAddress() {
        return qlcAddress;
    }

    public void setQlcAddress(String qlcAddress) {
        this.qlcAddress = qlcAddress;
    }

    //手持身份证照
    private String holdingPhoto;
    @Generated(hash = 201896621)
    public UserAccount(Long id, String account, String password, String pubKey,
            boolean isLogin, String inviteCode, String userName, String avatar,
            String test, String bindDate, String phone, String userId, String email,
            String vstatus, String facePhoto, int totalInvite, String qlcAddress,
            boolean startApp, String holdingPhoto) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.pubKey = pubKey;
        this.isLogin = isLogin;
        this.inviteCode = inviteCode;
        this.userName = userName;
        this.avatar = avatar;
        this.test = test;
        this.bindDate = bindDate;
        this.phone = phone;
        this.userId = userId;
        this.email = email;
        this.vstatus = vstatus;
        this.facePhoto = facePhoto;
        this.totalInvite = totalInvite;
        this.qlcAddress = qlcAddress;
        this.startApp = startApp;
        this.holdingPhoto = holdingPhoto;
    }

    @Generated(hash = 1029142458)
    public UserAccount() {
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
    public boolean getIsLogin() {
        return this.isLogin;
    }
    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
    public String getInviteCode() {
        return this.inviteCode;
    }
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getVstatus() {
        return this.vstatus;
    }
    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    }
    public String getFacePhoto() {
        return this.facePhoto;
    }
    public void setFacePhoto(String facePhoto) {
        this.facePhoto = facePhoto;
    }
    public String getHoldingPhoto() {
        return this.holdingPhoto;
    }
    public void setHoldingPhoto(String holdingPhoto) {
        this.holdingPhoto = holdingPhoto;
    }

    public String getTest() {
        return this.test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getBindDate() {
        return this.bindDate;
    }

    public void setBindDate(String bindDate) {
        this.bindDate = bindDate;
    }

    public boolean getStartApp() {
        return this.startApp;
    }

    public void setStartApp(boolean startApp) {
        this.startApp = startApp;
    }
}
