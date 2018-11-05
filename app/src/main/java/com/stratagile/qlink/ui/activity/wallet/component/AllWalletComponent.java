package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.AllWalletFragment;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for AllWalletFragment
 * @date 2018/10/24 10:17:57
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = AllWalletModule.class)
public interface AllWalletComponent {
    AllWalletFragment inject(AllWalletFragment Fragment);
}