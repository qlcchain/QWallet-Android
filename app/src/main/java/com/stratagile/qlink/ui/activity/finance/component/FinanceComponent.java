package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.FinanceFragment;
import com.stratagile.qlink.ui.activity.finance.module.FinanceModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for FinanceFragment
 * @date 2019/04/08 17:36:49
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = FinanceModule.class)
public interface FinanceComponent {
    FinanceFragment inject(FinanceFragment Fragment);
}