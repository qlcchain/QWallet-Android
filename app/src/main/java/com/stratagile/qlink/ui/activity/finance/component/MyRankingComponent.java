package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.MyRankingActivity;
import com.stratagile.qlink.ui.activity.finance.module.MyRankingModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for MyRankingActivity
 * @date 2019/04/24 11:14:23
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MyRankingModule.class)
public interface MyRankingComponent {
    MyRankingActivity inject(MyRankingActivity Activity);
}