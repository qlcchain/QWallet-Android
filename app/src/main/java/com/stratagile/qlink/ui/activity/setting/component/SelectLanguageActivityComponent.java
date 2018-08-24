package com.stratagile.qlink.ui.activity.setting.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.setting.SelectLanguageActivityActivity;
import com.stratagile.qlink.ui.activity.setting.module.SelectLanguageActivityModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: The component for SelectLanguageActivityActivity
 * @date 2018/06/26 17:11:28
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SelectLanguageActivityModule.class)
public interface SelectLanguageActivityComponent {
    SelectLanguageActivityActivity inject(SelectLanguageActivityActivity Activity);
}