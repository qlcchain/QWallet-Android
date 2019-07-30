package com.stratagile.qlink.entity;

import java.util.List;

public class KLine extends BaseBack<List<List<String>>> {

//    private List<List<String>> data;
//
//    public List<List<String>> getData() {
//        return data;
//    }
//
//    public void setData(List<List<String>> data) {
//        this.data = data;
//    }

    public static class Line {
        private long openTime;
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;
        private long unkown1;
        private String unkown2;
        private float trades;
    }
}
