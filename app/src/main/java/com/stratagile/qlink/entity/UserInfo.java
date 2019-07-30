package com.stratagile.qlink.entity;

import com.google.gson.annotations.SerializedName;

public class UserInfo extends BaseBack<UserInfo.DataBean> {

    /**
     * data : {"head":"/data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg","number":"19528463","phone":"18670819116","facePhoto":"/data/dapp/head/58b574e39225445999677fc60be3490d.jpg","nickname":"hzp","totalInvite":1,"vStatus":"KYC_SUCCESS","id":"7060628a65e4450690976bf56c127787","account":"18670819116","email":"","otcTimes":6,"holdingPhoto":"/data/dapp/head/44ca676639dd499baaaa3b55c5035375.jpg"}
     */

    public static class DataBean {
        /**
         * head : /data/dapp/head/cd67f44e4b8a428e8660356e9463e693.jpg
         * number : 19528463
         * phone : 18670819116
         * facePhoto : /data/dapp/head/58b574e39225445999677fc60be3490d.jpg
         * nickname : hzp
         * totalInvite : 1
         * vStatus : KYC_SUCCESS
         * id : 7060628a65e4450690976bf56c127787
         * account : 18670819116
         * email :
         * otcTimes : 6
         * holdingPhoto : /data/dapp/head/44ca676639dd499baaaa3b55c5035375.jpg
         */

        private String head;
        private String number;
        private String phone;
        private String facePhoto;
        private String nickname;
        private int totalInvite;
        private String vStatus;
        private String id;
        private String account;
        private String email;
        private int otcTimes;
        private String holdingPhoto;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getFacePhoto() {
            return facePhoto;
        }

        public void setFacePhoto(String facePhoto) {
            this.facePhoto = facePhoto;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getTotalInvite() {
            return totalInvite;
        }

        public void setTotalInvite(int totalInvite) {
            this.totalInvite = totalInvite;
        }

        public String getVStatus() {
            return vStatus;
        }

        public void setVStatus(String vStatus) {
            this.vStatus = vStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getOtcTimes() {
            return otcTimes;
        }

        public void setOtcTimes(int otcTimes) {
            this.otcTimes = otcTimes;
        }

        public String getHoldingPhoto() {
            return holdingPhoto;
        }

        public void setHoldingPhoto(String holdingPhoto) {
            this.holdingPhoto = holdingPhoto;
        }
    }
}
