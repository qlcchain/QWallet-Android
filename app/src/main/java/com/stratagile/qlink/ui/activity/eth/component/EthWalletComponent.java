package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWalletActivity;
import com.stratagile.qlink.ui.activity.eth.module.EthWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthWalletActivity
 * @date 2018/05/24 10:30:26
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthWalletModule.class)
public interface EthWalletComponent {
    EthWalletActivity inject(EthWalletActivity Activity);
}