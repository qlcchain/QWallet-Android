package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletListFragment;
import com.stratagile.qlink.ui.activity.wallet.module.WalletListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for WalletListFragment
 * @date 2018/01/09 15:08:22
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WalletListModule.class)
public interface WalletListComponent {
    WalletListFragment inject(WalletListFragment Fragment);
}