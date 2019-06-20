package com.stratagile.qlink.ui.activity.otc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.otc.AddTokenActivity;
import com.stratagile.qlink.ui.activity.otc.module.AddTokenModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: The component for AddTokenActivity
 * @date 2018/11/28 11:03:57
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = AddTokenModule.class)
public interface AddTokenComponent {
    AddTokenActivity inject(AddTokenActivity Activity);
}