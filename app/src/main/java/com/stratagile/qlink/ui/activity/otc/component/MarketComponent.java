package com.stratagile.qlink.ui.activity.otc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.otc.MarketFragment;
import com.stratagile.qlink.ui.activity.otc.module.MarketModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: The component for MarketFragment
 * @date 2019/06/14 16:23:19
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MarketModule.class)
public interface MarketComponent {
    MarketFragment inject(MarketFragment Fragment);
}