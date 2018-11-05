package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.AllWalletTokenActivity;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletTokenModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for AllWalletTokenActivity
 * @date 2018/10/24 15:49:29
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = AllWalletTokenModule.class)
public interface AllWalletTokenComponent {
    AllWalletTokenActivity inject(AllWalletTokenActivity Activity);
}