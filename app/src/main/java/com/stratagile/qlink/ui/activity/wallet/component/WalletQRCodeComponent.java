package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletQRCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.module.WalletQRCodeModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for WalletQRCodeActivity
 * @date 2018/10/30 15:54:27
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WalletQRCodeModule.class)
public interface WalletQRCodeComponent {
    WalletQRCodeActivity inject(WalletQRCodeActivity Activity);
}