package com.stratagile.qlink.ui.activity.rank.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.rank.RankListFragment;
import com.stratagile.qlink.ui.activity.rank.module.RankListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: The component for RankListFragment
 * @date 2018/07/31 18:09:12
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RankListModule.class)
public interface RankListComponent {
    RankListFragment inject(RankListFragment Fragment);
}