package com.stratagile.qlink.entity;

import java.util.List;

public class ReportList extends BaseBack {

    private List<SmsReport.ReportBean> smsList;

    public List<SmsReport.ReportBean> getSmsList() {
        return smsList;
    }

    public void setSmsList(List<SmsReport.ReportBean> smsList) {
        this.smsList = smsList;
    }

    public static class SmsListBean {
        /**
         * number : 901789055
         * qgasAmount : 1.0
         * smsHash : 23f7d620d9abbbbcc8a72cbb79a8897e
         * qgasHash : a2f045503c500461d4f18ab20dc9d74060a164dc110571e65add23e8cdd4fdcc
         * userAccount : 18670819116
         * isp : 中国移动
         * sms : 尊敬的客户，您好！根据您的授权查询，您于近15天内曾到访：湖南（长沙），江西（吉安）；16-30天内曾到访：江西（吉安）。查询信息仅供参考，不排除边界漫游的可能，不作为最终判定依据。请规范使用，注意保密。【中国移动】
         * phoneHash : 78e7831a29bce0837fce80c8bb532090
         * id : 2c8ce94827f04ed3a09aad3bbd792154
         * createDate : 2020-02-21 21:33:47
         */

        private String number;
        private double qgasAmount;
        private String smsHash;
        private String qgasHash;
        private String userAccount;
        private String isp;
        private String sms;
        private String phoneHash;
        private String id;
        private String createDate;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public double getQgasAmount() {
            return qgasAmount;
        }

        public void setQgasAmount(double qgasAmount) {
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
    }
}
