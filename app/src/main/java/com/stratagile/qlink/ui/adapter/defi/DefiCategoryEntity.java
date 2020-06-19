package com.stratagile.qlink.ui.adapter.defi;

public class DefiCategoryEntity {
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public DefiCategoryEntity(String categoryName, boolean select) {
        this.categoryName = categoryName;
        this.select = select;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    private boolean select;
}
