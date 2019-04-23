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
    //手机号码
    private String phone;

    private String email;
    @Generated(hash = 351281387)
    public UserAccount(Long id, String account, String password, String pubKey,
            boolean isLogin, String inviteCode, String userName, String avatar,
            String phone, String email) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.pubKey = pubKey;
        this.isLogin = isLogin;
        this.inviteCode = inviteCode;
        this.userName = userName;
        this.avatar = avatar;
        this.phone = phone;
        this.email = email;
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
}
