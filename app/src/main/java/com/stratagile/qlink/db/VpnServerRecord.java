package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by huzhipeng on 2018/2/7.
 * vpn的实体类
 */

@Entity
public class VpnServerRecord implements Parcelable, Comparable<VpnServerRecord> {
    @Id(autoincrement = true)
    private Long id;



    private String p2pId;

    private String vpnName;

    private String vpnfileName;

    private String userName;

    private String password;


    protected VpnServerRecord(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        p2pId = in.readString();
        vpnName = in.readString();
        vpnfileName = in.readString();
        userName = in.readString();
        password = in.readString();
        privateKey = in.readString();
        isMainNet = in.readByte() != 0;
    }

    @Generated(hash = 302748118)
    public VpnServerRecord(Long id, String p2pId, String vpnName, String vpnfileName,
            String userName, String password, String privateKey, boolean isMainNet) {
        this.id = id;
        this.p2pId = p2pId;
        this.vpnName = vpnName;
        this.vpnfileName = vpnfileName;
        this.userName = userName;
        this.password = password;
        this.privateKey = privateKey;
        this.isMainNet = isMainNet;
    }

    @Generated(hash = 1303301461)
    public VpnServerRecord() {
    }

    public static final Creator<VpnServerRecord> CREATOR = new Creator<VpnServerRecord>() {
        @Override
        public VpnServerRecord createFromParcel(Parcel in) {
            return new VpnServerRecord(in);
        }

        @Override
        public VpnServerRecord[] newArray(int size) {
            return new VpnServerRecord[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }
    private String privateKey;

    public boolean isMainNet() {
        return isMainNet;
    }

    public void setMainNet(boolean mainNet) {
        isMainNet = mainNet;
    }

    private boolean isMainNet;//是否主网

    @Override
    public String toString() {
        return "VpnServerRecord{" +
                "id=" + id +
                ", p2pId='" + p2pId + '\'' +
                ", vpnName='" + vpnName + '\'' +
                ", vpnfileName='" + vpnfileName + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", isMainNet=" + isMainNet +
                '}';
    }

    public String getVpnName() {
        return this.vpnName;
    }

    public void setVpnName(String vpnName) {
        this.vpnName = vpnName;
    }

    public String getVpnfileName() {
        return this.vpnfileName;
    }

    public void setVpnfileName(String vpnfileName) {
        this.vpnfileName = vpnfileName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeString(p2pId);
        parcel.writeString(vpnName);
        parcel.writeString(vpnfileName);
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(privateKey);
        parcel.writeByte((byte) (isMainNet ? 1 : 0));
    }

    @Override
    public int compareTo(@NonNull VpnServerRecord vpnServerRecord) {
        return 0;
    }

    public boolean getIsMainNet() {
        return this.isMainNet;
    }

    public void setIsMainNet(boolean isMainNet) {
        this.isMainNet = isMainNet;
    }
}
