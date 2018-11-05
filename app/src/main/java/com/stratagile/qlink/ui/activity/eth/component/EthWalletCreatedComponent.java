package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.eth.module.EthWalletCreatedModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthWalletCreatedActivity
 * @date 2018/10/23 15:15:28
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthWalletCreatedModule.class)
public interface EthWalletCreatedComponent {
    EthWalletCreatedActivity inject(EthWalletCreatedActivity Activity);
}