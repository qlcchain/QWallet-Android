package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletFragment;
import com.stratagile.qlink.ui.activity.wallet.module.WalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for WalletFragment
 * @date 2018/01/18 19:08:00
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WalletModule.class)
public interface WalletComponent {
    WalletFragment inject(WalletFragment Fragment);
}