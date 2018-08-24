package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.SelectCountryActivity;
import com.stratagile.qlink.ui.activity.vpn.module.SelectCountryModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for SelectCountryActivity
 * @date 2018/02/07 11:01:05
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SelectCountryModule.class)
public interface SelectCountryComponent {
    SelectCountryActivity inject(SelectCountryActivity Activity);
}