package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletCreatedActivity;
import com.stratagile.qlink.ui.activity.wallet.module.WalletCreatedModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for WalletCreatedActivity
 * @date 2018/01/24 16:27:17
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WalletCreatedModule.class)
public interface WalletCreatedComponent {
    WalletCreatedActivity inject(WalletCreatedActivity Activity);
}