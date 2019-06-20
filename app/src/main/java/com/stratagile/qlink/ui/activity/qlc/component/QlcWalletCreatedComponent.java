package com.stratagile.qlink.ui.activity.qlc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.QlcWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.qlc.module.QlcWalletCreatedModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for QlcWalletCreatedActivity
 * @date 2019/05/20 09:24:16
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = QlcWalletCreatedModule.class)
public interface QlcWalletCreatedComponent {
    QlcWalletCreatedActivity inject(QlcWalletCreatedActivity Activity);
}