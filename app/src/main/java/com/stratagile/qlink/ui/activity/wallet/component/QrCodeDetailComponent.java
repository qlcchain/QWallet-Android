package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.QrCodeDetailActivity;
import com.stratagile.qlink.ui.activity.wallet.module.QrCodeDetailModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for QrCodeDetailActivity
 * @date 2018/02/28 15:16:22
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = QrCodeDetailModule.class)
public interface QrCodeDetailComponent {
    QrCodeDetailActivity inject(QrCodeDetailActivity Activity);
}