package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.module.NoWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for NoWalletActivity
 * @date 2018/01/23 13:54:18
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = NoWalletModule.class)
public interface NoWalletComponent {
    NoWalletActivity inject(NoWalletActivity Activity);
}