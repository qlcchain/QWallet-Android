package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.SelectCountryActivity;
import com.stratagile.qlink.ui.activity.my.module.SelectCountryModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for SelectCountryActivity
 * @date 2019/04/09 14:38:53
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SelectCountryModule.class)
public interface SelectCountryComponent {
    SelectCountryActivity inject(SelectCountryActivity Activity);
}