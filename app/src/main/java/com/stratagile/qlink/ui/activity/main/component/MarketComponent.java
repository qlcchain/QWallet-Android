package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.MarketFragment;
import com.stratagile.qlink.ui.activity.main.module.MarketModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for MarketFragment
 * @date 2018/10/25 15:54:02
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MarketModule.class)
public interface MarketComponent {
    MarketFragment inject(MarketFragment Fragment);
}