package com.stratagile.qlink.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SmsReport extends BaseBack {

    /**
     * report : {"number":"689260301","qgasAmount":"10","smsHash":"smsHash","qgasHash":"ASgSVJFhH3TTx5Q4UkdMLGeYYUFxKPzi2","userAccount":"14f501e38eba454fb0b69044f4b57e86","isp":"中国移动","sms":"sms","phoneHash":"phoneHash","id":"98ac1e4c489043e3b34719e83fca3149","createDate":"2020-02-20 23:09:59"}
     */

    private ReportBean report;

    public ReportBean getReport() {
        return report;
    }

    public void setReport(ReportBean report) {
        this.report = report;
    }

    public static class ReportBean implements Parcelable {
        /**
         * number : 689260301
         * qgasAmount : 10
         * smsHash : smsHash
         * qgasHash : ASgSVJFhH3TTx5Q4UkdMLGeYYUFxKPzi2
         * userAccount : 14f501e38eba454fb0b69044f4b57e86
         * isp : 中国移动
         * sms : sms
         * phoneHash : phoneHash
         * id : 98ac1e4c489043e3b34719e83fca3149
         * createDate : 2020-02-20 23:09:59
         */

        private String number;
        private String qgasAmount;
        private String smsHash;
        private String qgasHash;
        private String userAccount;
        private String isp;
        private String sms;
        private String phoneHash;
        private String id;
        private String createDate;

        protected ReportBean(Parcel in) {
            number = in.readString();
            qgasAmount = in.readString();
            smsHash = in.readString();
            qgasHash = in.readString();
            userAccount = in.readString();
            isp = in.readString();
            sms = in.readString();
            phoneHash = in.readString();
            id = in.readString();
            createDate = in.readString();
        }

        public static final Creator<ReportBean> CREATOR = new Creator<ReportBean>() {
            @Override
            public ReportBean createFromParcel(Parcel in) {
                return new ReportBean(in);
            }

            @Override
            public ReportBean[] newArray(int size) {
                return new ReportBean[size];
            }
        };

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(String qgasAmount) {
            this.qgasAmount = qgasAmount;
        }

        public String getSmsHash() {
            return smsHash;
        }

        public void setSmsHash(String smsHash) {
            this.smsHash = smsHash;
        }

        public String getQgasHash() {
            return qgasHash;
        }

        public void setQgasHash(String qgasHash) {
            this.qgasHash = qgasHash;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getSms() {
            return sms;
        }

        public void setSms(String sms) {
            this.sms = sms;
        }

        public String getPhoneHash() {
            return phoneHash;
        }

        public void setPhoneHash(String phoneHash) {
            this.phoneHash = phoneHash;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(number);
            dest.writeString(qgasAmount);
            dest.writeString(smsHash);
            dest.writeString(qgasHash);
            dest.writeString(userAccount);
            dest.writeString(isp);
            dest.writeString(sms);
            dest.writeString(phoneHash);
            dest.writeString(id);
            dest.writeString(createDate);
        }
    }
}
