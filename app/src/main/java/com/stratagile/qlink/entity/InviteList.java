package com.stratagile.qlink.entity;

import java.util.List;

public class InviteList extends BaseBack {

    /**
     * myInfo : {"head":"/data/dapp/head/1b2375c0388d45a5997631da94f20103.jpg","name":"18670819116","totalInvite":0,"id":"19528463","myRanking":0}
     * top5 : [{"head":"/data/dapp/head/fab2931e6a344943a6713ffa9c8dccf7.png","sequence":1,"name":"Jelly","totalInvite":1,"id":"06812349"}]
     * guanggaoList : [{"imgPath":"/userfiles/1/images/guanggao/2019/04/3e3b05bd1a4a45e3aed0d4954aaaa829.png","url":""}]
     */

    private MyInfoBean myInfo;
    private List<Top5Bean> top5;
    private List<GuanggaoListBean> guanggaoList;

    public MyInfoBean getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(MyInfoBean myInfo) {
        this.myInfo = myInfo;
    }

    public List<Top5Bean> getTop5() {
        return top5;
    }

    public void setTop5(List<Top5Bean> top5) {
        this.top5 = top5;
    }

    public List<GuanggaoListBean> getGuanggaoList() {
        return guanggaoList;
    }

    public void setGuanggaoList(List<GuanggaoListBean> guanggaoList) {
        this.guanggaoList = guanggaoList;
    }

    public static class MyInfoBean {
        /**
         * head : /data/dapp/head/1b2375c0388d45a5997631da94f20103.jpg
         * name : 18670819116
         * totalInvite : 0
         * id : 19528463
         * myRanking : 0
         */

        private String head;
        private String name;
        private int totalInvite;
        private String id;
        private int myRanking;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalInvite() {
            return totalInvite;
        }

        public void setTotalInvite(int totalInvite) {
            this.totalInvite = totalInvite;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getMyRanking() {
            return myRanking;
        }

        public void setMyRanking(int myRanking) {
            this.myRanking = myRanking;
        }
    }

    public static class Top5Bean {
        /**
         * head : /data/dapp/head/fab2931e6a344943a6713ffa9c8dccf7.png
         * sequence : 1
         * name : Jelly
         * totalInvite : 1
         * id : 06812349
         */

        private String head;
        private int sequence;
        private String name;
        private int totalInvite;
        private String id;

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTotalInvite() {
            return totalInvite;
        }

        public void setTotalInvite(int totalInvite) {
            this.totalInvite = totalInvite;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public static class GuanggaoListBean {
        /**
         * imgPath : /userfiles/1/images/guanggao/2019/04/3e3b05bd1a4a45e3aed0d4954aaaa829.png
         * url :
         */

        private String imgPath;
        private String imgPathEn;

        public String getImgPathEn() {
            return imgPathEn;
        }

        public void setImgPathEn(String imgPathEn) {
            this.imgPathEn = imgPathEn;
        }

        private String url;

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
