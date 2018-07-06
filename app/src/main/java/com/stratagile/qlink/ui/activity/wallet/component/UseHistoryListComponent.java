package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.UseHistoryListFragment;
import com.stratagile.qlink.ui.activity.wallet.module.UseHistoryListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for UseHistoryListFragment
 * @date 2018/01/19 11:44:00
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = UseHistoryListModule.class)
public interface UseHistoryListComponent {
    UseHistoryListFragment inject(UseHistoryListFragment Fragment);
}