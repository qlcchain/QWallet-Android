package com.stratagile.qlink.ui.activity.setting.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.setting.CurrencyUnitActivity;
import com.stratagile.qlink.ui.activity.setting.module.CurrencyUnitModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: The component for CurrencyUnitActivity
 * @date 2018/10/29 14:00:24
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = CurrencyUnitModule.class)
public interface CurrencyUnitComponent {
    CurrencyUnitActivity inject(CurrencyUnitActivity Activity);
}