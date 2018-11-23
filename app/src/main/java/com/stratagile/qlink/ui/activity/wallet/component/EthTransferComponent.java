package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.EthTransferActivity;
import com.stratagile.qlink.ui.activity.wallet.module.EthTransferModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for EthTransferActivity
 * @date 2018/10/31 10:17:17
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthTransferModule.class)
public interface EthTransferComponent {
    EthTransferActivity inject(EthTransferActivity Activity);
}