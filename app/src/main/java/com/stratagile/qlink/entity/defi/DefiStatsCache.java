package com.stratagile.qlink.entity.defi;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class DefiStatsCache extends BaseBack {

    private List<StatsCacheBean> statsCache;

    public List<StatsCacheBean> getStatsCache() {
        return statsCache;
    }

    public void setStatsCache(List<StatsCacheBean> statsCache) {
        this.statsCache = statsCache;
    }

    public static class StatsCacheBean {
        /**
         * cache : [1.4029706E7,1.4214619E7,1.4189615E7,1.3961949E7,1.4264624E7,1.5397022E7,4268277,1380454,2644897,5520917]
         * id : e864a4ebb087411f9ce5269ae63324ff
         */

        private String id;
        private List<Double> cache;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Double> getCache() {
            return cache;
        }

        public void setCache(List<Double> cache) {
            this.cache = cache;
        }
    }
}
