package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.module.CreateWalletPasswordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for CreateWalletPasswordActivity
 * @date 2018/01/24 17:48:46
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = CreateWalletPasswordModule.class)
public interface CreateWalletPasswordComponent {
    CreateWalletPasswordActivity inject(CreateWalletPasswordActivity Activity);
}