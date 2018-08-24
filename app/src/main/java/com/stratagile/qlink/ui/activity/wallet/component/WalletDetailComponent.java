package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletDetailActivity;
import com.stratagile.qlink.ui.activity.wallet.module.WalletDetailModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for WalletDetailActivity
 * @date 2018/01/19 10:21:00
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WalletDetailModule.class)
public interface WalletDetailComponent {
    WalletDetailActivity inject(WalletDetailActivity Activity);
}