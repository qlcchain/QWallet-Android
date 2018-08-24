package com.stratagile.qlink.entity.vpn;

/**
 * Created by huzhipeng on 2018/2/9.
 * 当使用者连接vpn时，返回vpn配置文件的用户名和密码
 */

public class VpnUserAndPasswordRsp {
    private String vpnName;
    private String userName;
    private String password;

    @Override
    public String toString() {
        return "VpnUserAndPasswordRsp{" +
                "vpnName='" + vpnName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getVpnName() {
        return vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
