package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.SelectContinentActivity;
import com.stratagile.qlink.ui.activity.vpn.module.SelectContinentModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for SelectContinentActivity
 * @date 2018/03/12 16:57:42
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SelectContinentModule.class)
public interface SelectContinentComponent {
    SelectContinentActivity inject(SelectContinentActivity Activity);
}