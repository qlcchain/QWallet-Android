package com.stratagile.qlink.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DefiSearchHistory {
    @Id(autoincrement = true)
    private Long id;
    private String url;
    @Generated(hash = 342368708)
    public DefiSearchHistory(Long id, String url) {
        this.id = id;
        this.url = url;
    }
    @Generated(hash = 956239227)
    public DefiSearchHistory() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
}
