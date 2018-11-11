package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ChooseWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ChooseWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ChooseWalletActivity
 * @date 2018/11/06 11:21:13
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ChooseWalletModule.class)
public interface ChooseWalletComponent {
    ChooseWalletActivity inject(ChooseWalletActivity Activity);
}