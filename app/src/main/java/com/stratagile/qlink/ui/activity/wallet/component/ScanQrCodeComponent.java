package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ScanQrCodeModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ScanQrCodeActivity
 * @date 2018/03/05 17:42:26
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ScanQrCodeModule.class)
public interface ScanQrCodeComponent {
    ScanQrCodeActivity inject(ScanQrCodeActivity Activity);
}