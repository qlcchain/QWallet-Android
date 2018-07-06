package com.stratagile.qlink.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by huzhipeng on 2018/1/18.
 */

@Entity
public class WifiMyRegiste {
    @Id(autoincrement = true)
    private Long id;

    @Generated(hash = 293688171)
    public WifiMyRegiste(Long id) {
        this.id = id;
    }

    @Generated(hash = 626031726)
    public WifiMyRegiste() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
