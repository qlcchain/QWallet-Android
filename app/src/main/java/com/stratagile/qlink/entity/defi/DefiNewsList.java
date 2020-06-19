package com.stratagile.qlink.entity.defi;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class DefiNewsList extends BaseBack {

    private List<NewsListBean> newsList;

    public List<NewsListBean> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsListBean> newsList) {
        this.newsList = newsList;
    }

    public static class NewsListBean {
        /**
         * authod : Julia Magas
         * leadText : Security, a viable business model and an active community: why the majority of DApp developers still opt for the Ethereum blockchain.
         * id : 585f55420e3740c198d07c4966998cd2
         * title : Ethereum May Not Be Perfect, but Most DApps Still Like to Run With It
         * views : 961
         * createDate : 2020-06-04 08:00:00
         */

        private String authod;
        private String leadText;
        private String id;
        private String title;
        private int views;
        private String createDate;

        public String getAuthod() {
            return authod;
        }

        public void setAuthod(String authod) {
            this.authod = authod;
        }

        public String getLeadText() {
            return leadText;
        }

        public void setLeadText(String leadText) {
            this.leadText = leadText;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getViews() {
            return views;
        }

        public void setViews(int views) {
            this.views = views;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }
    }
}
