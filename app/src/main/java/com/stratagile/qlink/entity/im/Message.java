package com.stratagile.qlink.entity.im;

/**
 * Created by huzhipeng on 2018/3/20.
 */

public class Message {
    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息方向，0为别人发的，1为自己发的
     */
    private int direction;

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", direction=" + direction +
                ", groupNum=" + groupNum +
                ", nickName='" + nickName + '\'' +
                ", avatarUpdateTime='" + avatarUpdateTime + '\'' +
                ", messageTime=" + messageTime +
                ", avatar='" + avatar + '\'' +
                ", assetType=" + assetType +
                ", assetName='" + assetName + '\'' +
                ", p2pId='" + p2pId + '\'' +
                '}';
    }

    private int groupNum;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 自己头像的时间戳
     */
    private String avatarUpdateTime;

    /**
     * 消息的时间戳
     */
    private long messageTime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private String avatar;

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUpdateTime() {
        return avatarUpdateTime;
    }

    public void setAvatarUpdateTime(String avatarUpdateTime) {
        this.avatarUpdateTime = avatarUpdateTime;
    }

    /**
     *
     */
    private int assetType;

    public int getAssetType() {
        return assetType;
    }

    public void setAssetType(int assetType) {
        this.assetType = assetType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    private String assetName;

    public String getP2pId() {
        return p2pId;
    }

    public void setP2pId(String p2pId) {
        this.p2pId = p2pId;
    }

    private String p2pId;

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Message(String content) {

        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
