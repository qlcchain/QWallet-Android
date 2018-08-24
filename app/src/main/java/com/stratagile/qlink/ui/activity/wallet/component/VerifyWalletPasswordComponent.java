package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.module.VerifyWalletPasswordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for VerifyWalletPasswordActivity
 * @date 2018/01/24 18:19:18
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = VerifyWalletPasswordModule.class)
public interface VerifyWalletPasswordComponent {
    VerifyWalletPasswordActivity inject(VerifyWalletPasswordActivity Activity);
}