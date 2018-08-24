package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ChangeWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ChangeWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ChangeWalletActivity
 * @date 2018/03/05 11:36:39
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ChangeWalletModule.class)
public interface ChangeWalletComponent {
    ChangeWalletActivity inject(ChangeWalletActivity Activity);
}