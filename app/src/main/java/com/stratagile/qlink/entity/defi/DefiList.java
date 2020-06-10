package com.stratagile.qlink.entity.defi;

import android.os.Parcel;
import android.os.Parcelable;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class DefiList extends BaseBack {

    private List<ProjectListBean> projectList;

    public List<ProjectListBean> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectListBean> projectList) {
        this.projectList = projectList;
    }

    public static class ProjectListBean implements Parcelable {
        /**
         * chain : Ethereum
         * name : dForce
         * rating : 7
         * id : 8d74818a68a947c0b7225e5be03714f9
         * shortName :
         * category : Lending
         * tvlUsd : 0E-12
         */

        private String chain;
        private String name;
        private String rating;
        private String id;
        private String shortName;
        private String category;
        private String tvlUsd;
        private DefiStatsCache.StatsCacheBean statsCache;

        public DefiStatsCache.StatsCacheBean getStatsCache() {
            return statsCache;
        }

        public void setStatsCache(DefiStatsCache.StatsCacheBean statsCache) {
            this.statsCache = statsCache;
        }

        protected ProjectListBean(Parcel in) {
            chain = in.readString();
            name = in.readString();
            rating = in.readString();
            id = in.readString();
            shortName = in.readString();
            category = in.readString();
            tvlUsd = in.readString();
        }

        public static final Creator<ProjectListBean> CREATOR = new Creator<ProjectListBean>() {
            @Override
            public ProjectListBean createFromParcel(Parcel in) {
                return new ProjectListBean(in);
            }

            @Override
            public ProjectListBean[] newArray(int size) {
                return new ProjectListBean[size];
            }
        };

        public String getChain() {
            return chain;
        }

        public void setChain(String chain) {
            this.chain = chain;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTvlUsd() {
            return tvlUsd;
        }

        public void setTvlUsd(String tvlUsd) {
            this.tvlUsd = tvlUsd;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(chain);
            dest.writeString(name);
            dest.writeString(rating);
            dest.writeString(id);
            dest.writeString(shortName);
            dest.writeString(category);
            dest.writeString(tvlUsd);
        }
    }
}
