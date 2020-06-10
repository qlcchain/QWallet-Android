package com.stratagile.qlink.entity.defi;

import com.stratagile.qlink.entity.BaseBack;

import java.util.List;

public class DefiCategory extends BaseBack {

    private List<String> categoryList;

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}
