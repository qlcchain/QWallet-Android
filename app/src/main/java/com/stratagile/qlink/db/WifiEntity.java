package com.stratagile.qlink.db;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.socks.library.KLog;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by huzhipeng on 2018/1/18.
 * wifi的实体类
 */

@Entity
public class WifiEntity implements Parcelable, Comparable<WifiEntity> {
    @Id(autoincrement = true)
    private Long id;

    @Override
    public String toString() {
        return "WifiEntity{" +
                "id=" + id +
                ", ssid='" + ssid + '\'' +
                ", freindNum='" + freindNum + '\'' +
                ", groupNum=" + groupNum +
                ", avatar='" + avatar + '\'' +
                ", unReadMessageCount=" + unReadMessageCount +
                ", macAdrees='" + macAdrees + '\'' +
                ", lastFindTimeStamp='" + lastFindTimeStamp + '\'' +
                ", level=" + level +
                ", isRegiste=" + isRegiste +
                ", isRegisteByMe=" + isRegisteByMe +
                ", paymentType=" + paymentType +
                ", priceInQlc=" + priceInQlc +
                ", timeLimitPerDevice=" + timeLimitPerDevice +
                ", dailyTotalTimeLimit=" + dailyTotalTimeLimit +
                ", capabilities='" + capabilities + '\'' +
                ", priceMode=" + priceMode +
                ", deviceAllowed=" + deviceAllowed +
                ", connectCount=" + connectCount +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", isFriend=" + isFriend +
                ", ownerP2PId='" + ownerP2PId + '\'' +
                ", wifiPassword='" + wifiPassword + '\'' +
                ", online=" + online +
                ", avaterUpdateTime=" + avaterUpdateTime +
                ", isLoadingAvater=" + isLoadingAvater +
                ", walletAddress='" + walletAddress + '\'' +
                ", isConnected=" + isConnected +
                ", assetTranfer=" + assetTranfer +
                ", registerQlc=" + registerQlc +
                ", isInMainWallet=" + isInMainWallet +
                '}';
    }

    /**
     * WiFi的ssid
     */
    private String ssid;


    protected WifiEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        ssid = in.readString();
        freindNum = in.readString();
        groupNum = in.readInt();
        avatar = in.readString();
        unReadMessageCount = in.readInt();
        macAdrees = in.readString();
        lastFindTimeStamp = in.readString();
        level = in.readInt();
        isRegiste = in.readByte() != 0;
        isRegisteByMe = in.readByte() != 0;
        paymentType = in.readInt();
        priceInQlc = in.readFloat();
        timeLimitPerDevice = in.readInt();
        dailyTotalTimeLimit = in.readInt();
        capabilities = in.readString();
        priceMode = in.readInt();
        deviceAllowed = in.readInt();
        connectCount = in.readInt();
        longitude = in.readFloat();
        latitude = in.readFloat();
        isFriend = in.readByte() != 0;
        ownerP2PId = in.readString();
        wifiPassword = in.readString();
        online = in.readByte() != 0;
        avaterUpdateTime = in.readLong();
        isLoadingAvater = in.readByte() != 0;
        walletAddress = in.readString();
        isConnected = in.readByte() != 0;
        assetTranfer = in.readDouble();
        registerQlc = in.readDouble();
        isInMainWallet = in.readByte() != 0;

    }

    public static final Creator<WifiEntity> CREATOR = new Creator<WifiEntity>() {
        @Override
        public WifiEntity createFromParcel(Parcel in) {
            return new WifiEntity(in);
        }

        @Override
        public WifiEntity[] newArray(int size) {
            return new WifiEntity[size];
        }
    };

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 好友编号
     */
    private String freindNum;


    private int groupNum;

    private String avatar;


    public int getUnReadMessageCount() {
        return unReadMessageCount;
    }

    public void setUnReadMessageCount(int unReadMessageCount) {
        this.unReadMessageCount = unReadMessageCount;
    }

    /**
     * 未读消息的个数


     */
    private int unReadMessageCount;

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }



    /**
     * WiFi的mac地址
     */
    private String macAdrees;

    /**
     * 上次连接的时间戳
     */
    private String lastFindTimeStamp;

    /**
     * 信号强度
     */
    private int level;


    /**
     * 是否注册
     */
    private boolean isRegiste;

    /**
     * @deprecated
     */
    private boolean isRegisteByMe;

    /**
     * 0 = per Conncetion, 1 = per Hour
     * @deprecated
     */
    private int paymentType;

    /**
     * 期望得到的打赏金额
     */
    private float priceInQlc;

    /**
     * 每个设备每天能连接的最长时间
     */
    private int timeLimitPerDevice;

    /**
     * 每天能够连接的最大时长
     */
    private int dailyTotalTimeLimit;

    /**
     * WiFi的加密类型
     */
    private String capabilities;

    /**
     * 0 = free, 1 = tippingMode
     */
    private int priceMode;
    /**
     * 允许最大连接数
     */
    private int deviceAllowed;
    /**
     * 当前连接数
     */
    private int connectCount;

    /**
     * 经度
     */
    private float longitude;

    /**
     * 纬度
     */
    private float latitude;

    /**
     * 是否是好友，在第一次连接该WiFi的时候会自动加为好友
     */
    private boolean isFriend;

    /**
     * WiFi分享着的p2pId
     */
    private String ownerP2PId = "";

    /**
     * WiFi密码
     */
    private String wifiPassword;

    /**
     * 是否在线
     */
    private boolean online;

    private long avaterUpdateTime;

    /**
     * 是否正在下载头像
     *
     * @param in
     */

    private boolean isLoadingAvater;

    /**
     * 钱包地址
     */
    private String walletAddress;

    /**
     * 是否连接
     */
    private boolean isConnected;

    /**
     * 资产的价值，别人想要抢注册这个资产需要扣除的钱
     */
    private double assetTranfer;

    /**
     * 注册时押注的qlc数量
     */
    private double registerQlc;

    private boolean isInMainWallet = false;//是否主网资产

    public boolean isLoadingAvater() {
        return isLoadingAvater;
    }

    public void setLoadingAvater(boolean loadingAvater) {
        isLoadingAvater = loadingAvater;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }


    @Generated(hash = 1410964563)
    public WifiEntity() {
    }

    @Generated(hash = 1244294403)
    public WifiEntity(Long id, String ssid, String freindNum, int groupNum,
            String avatar, int unReadMessageCount, String macAdrees,
            String lastFindTimeStamp, int level, boolean isRegiste,
            boolean isRegisteByMe, int paymentType, float priceInQlc,
            int timeLimitPerDevice, int dailyTotalTimeLimit, String capabilities,
            int priceMode, int deviceAllowed, int connectCount, float longitude,
            float latitude, boolean isFriend, String ownerP2PId,
            String wifiPassword, boolean online, long avaterUpdateTime,
            boolean isLoadingAvater, String walletAddress, boolean isConnected,
            double assetTranfer, double registerQlc, boolean isInMainWallet) {
        this.id = id;
        this.ssid = ssid;
        this.freindNum = freindNum;
        this.groupNum = groupNum;
        this.avatar = avatar;
        this.unReadMessageCount = unReadMessageCount;
        this.macAdrees = macAdrees;
        this.lastFindTimeStamp = lastFindTimeStamp;
        this.level = level;
        this.isRegiste = isRegiste;
        this.isRegisteByMe = isRegisteByMe;
        this.paymentType = paymentType;
        this.priceInQlc = priceInQlc;
        this.timeLimitPerDevice = timeLimitPerDevice;
        this.dailyTotalTimeLimit = dailyTotalTimeLimit;
        this.capabilities = capabilities;
        this.priceMode = priceMode;
        this.deviceAllowed = deviceAllowed;
        this.connectCount = connectCount;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isFriend = isFriend;
        this.ownerP2PId = ownerP2PId;
        this.wifiPassword = wifiPassword;
        this.online = online;
        this.avaterUpdateTime = avaterUpdateTime;
        this.isLoadingAvater = isLoadingAvater;
        this.walletAddress = walletAddress;
        this.isConnected = isConnected;
        this.assetTranfer = assetTranfer;
        this.registerQlc = registerQlc;
        this.isInMainWallet = isInMainWallet;
    }

    public boolean isRegiste() {
        return isRegiste;
    }

    public void setRegiste(boolean registe) {
        isRegiste = registe;
    }

    public boolean isRegisteByMe() {
        return isRegisteByMe;
    }

    public void setRegisteByMe(boolean registeByMe) {
        isRegisteByMe = registeByMe;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isOnline() {
        return online;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getFreindNum() {
        return this.freindNum;
    }

    public void setFreindNum(String freindNum) {
        this.freindNum = freindNum;
    }

    public String getMacAdrees() {
        return this.macAdrees;
    }

    public void setMacAdrees(String macAdrees) {
        this.macAdrees = macAdrees;
    }

    public String getLastFindTimeStamp() {
        return this.lastFindTimeStamp;
    }

    public void setLastFindTimeStamp(String lastFindTimeStamp) {
        this.lastFindTimeStamp = lastFindTimeStamp;
    }

    public boolean getIsRegiste() {
        return this.isRegiste;
    }

    public void setIsRegiste(boolean isRegiste) {
        this.isRegiste = isRegiste;
    }

    public boolean getIsRegisteByMe() {
        return this.isRegisteByMe;
    }

    public void setIsRegisteByMe(boolean isRegisteByMe) {
        this.isRegisteByMe = isRegisteByMe;
    }

    public int getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public float getPriceInQlc() {
        return this.priceInQlc;
    }

    public void setPriceInQlc(float priceInQlc) {
        this.priceInQlc = priceInQlc;
    }

    public int getTimeLimitPerDevice() {
        return this.timeLimitPerDevice;
    }

    public void setTimeLimitPerDevice(int timeLimitPerDevice) {
        this.timeLimitPerDevice = timeLimitPerDevice;
    }

    public int getDailyTotalTimeLimit() {
        return this.dailyTotalTimeLimit;
    }

    public void setDailyTotalTimeLimit(int dailyTotalTimeLimit) {
        this.dailyTotalTimeLimit = dailyTotalTimeLimit;
    }

    public String getCapabilities() {
        return this.capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getPriceMode() {
        return this.priceMode;
    }

    public void setPriceMode(int priceMode) {
        this.priceMode = priceMode;
    }

    public int getDeviceAllowed() {
        return this.deviceAllowed;
    }

    public void setDeviceAllowed(int deviceAllowed) {
        this.deviceAllowed = deviceAllowed;
    }

    public int getConnectCount() {
        return this.connectCount;
    }

    public void setConnectCount(int connectCount) {
        this.connectCount = connectCount;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public boolean getIsFriend() {
        return this.isFriend;
    }

    public void setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
    }

    public String getOwnerP2PId() {
        return this.ownerP2PId;
    }

    public void setOwnerP2PId(String ownerP2PId) {
        this.ownerP2PId = ownerP2PId;
    }

    public String getWifiPassword() {
        return this.wifiPassword;
    }

    public void setWifiPassword(String wifiPassword) {
        this.wifiPassword = wifiPassword;
    }

    public boolean getOnline() {
        return this.online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }


    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public boolean getIsLoadingAvater() {
        return this.isLoadingAvater;
    }

    public void setIsLoadingAvater(boolean isLoadingAvater) {
        this.isLoadingAvater = isLoadingAvater;
    }

    public long getAvaterUpdateTime() {
        return this.avaterUpdateTime;
    }

    public void setAvaterUpdateTime(long avaterUpdateTime) {
        this.avaterUpdateTime = avaterUpdateTime;
    }

    /**
     * @param o
     * @return
     * @ 2018.03.02  比较是否连接
     */
    @Override
    public int compareTo(@NonNull WifiEntity o) {
        //先比较是否在线，再比较是否注册，再比较信号
        //先比较的满足条件的越在前
        int i;
        int myIsonline = isOnline() ? 1 : 0;
        int anohterIsonline = o.isOnline() ? 1 : 0;
        i = anohterIsonline - myIsonline; //比较是否在线
        if (i == 0) {
            int myIsRegiste = getOwnerP2PId().equals("") ? 0 : 1;
            int anotherIsRegiste = o.getOwnerP2PId().equals("") ? 0 : 1;
            i = anotherIsRegiste - myIsRegiste;
            if (i == 0) { //如果都在线或者不在线，比较信号
                int myIsonConnect = getIsConnected() ? 1 : 0;
                int anohterIsConnect = o.getIsConnected() ? 1 : 0;
                i = anohterIsConnect - myIsonConnect;
                if (i == 0) {
                    return o.level - level;
                }
                return i;

            } else {
                return i;
            }
        } else {
            return i;
        }
    }



    public boolean getIsConnected() {
        return this.isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public double getAssetTranfer() {
        return this.assetTranfer;
    }

    public void setAssetTranfer(double assetTranfer) {
        this.assetTranfer = assetTranfer;
    }

    public double getRegisterQlc() {
        return this.registerQlc;
    }

    public void setRegisterQlc(double registerQlc) {
        this.registerQlc = registerQlc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(ssid);
        dest.writeString(freindNum);
        dest.writeInt(groupNum);
        dest.writeString(avatar);
        dest.writeInt(unReadMessageCount);
        dest.writeString(macAdrees);
        dest.writeString(lastFindTimeStamp);
        dest.writeInt(level);
        dest.writeByte((byte) (isRegiste ? 1 : 0));
        dest.writeByte((byte) (isRegisteByMe ? 1 : 0));
        dest.writeInt(paymentType);
        dest.writeFloat(priceInQlc);
        dest.writeInt(timeLimitPerDevice);
        dest.writeInt(dailyTotalTimeLimit);
        dest.writeString(capabilities);
        dest.writeInt(priceMode);
        dest.writeInt(deviceAllowed);
        dest.writeInt(connectCount);
        dest.writeFloat(longitude);
        dest.writeFloat(latitude);
        dest.writeByte((byte) (isFriend ? 1 : 0));
        dest.writeString(ownerP2PId);
        dest.writeString(wifiPassword);
        dest.writeByte((byte) (online ? 1 : 0));
        dest.writeLong(avaterUpdateTime);
        dest.writeByte((byte) (isLoadingAvater ? 1 : 0));
        dest.writeString(walletAddress);
        dest.writeByte((byte) (isConnected ? 1 : 0));
        dest.writeDouble(assetTranfer);
        dest.writeDouble(registerQlc);
        dest.writeByte((byte) (isInMainWallet ? 1 : 0));
    }

    public boolean getIsInMainWallet() {
        return this.isInMainWallet;
    }

    public void setIsInMainWallet(boolean isInMainWallet) {
        this.isInMainWallet = isInMainWallet;
    }
}
