package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RankActivity;
import com.stratagile.qlink.ui.activity.vpn.module.RankModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for RankActivity
 * @date 2018/07/31 17:14:45
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RankModule.class)
public interface RankComponent {
    RankActivity inject(RankActivity Activity);
}