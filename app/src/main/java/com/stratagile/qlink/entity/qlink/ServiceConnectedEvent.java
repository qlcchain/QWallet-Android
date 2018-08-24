package com.stratagile.qlink.entity.qlink;

/**
 * Created by huzhipeng on 2018/1/31.
 */

public class ServiceConnectedEvent {
    /**
     * 事件类型，0 表示连接，1 表示心跳， 2 表示断开连接
     */
    private int eventType;

    /**
     * 使用的功能的类型， 0 表示wifi， 1表示vpn
     */
    private int useType;

    public ServiceConnectedEvent(int eventType, int useType, String ssid, String friendNum) {
        this.eventType = eventType;
        this.useType = useType;
        this.ssid = ssid;
        this.friendNum = friendNum;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getFriendNum() {
        return friendNum;
    }

    public void setFriendNum(String friendNum) {
        this.friendNum = friendNum;
    }

    /**
     * 用户使用的wifi的ssid
     */
    private String ssid;

    /**
     * 好友编号
     */
    private String friendNum;
}
