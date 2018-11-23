package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWalletDetailActivity;
import com.stratagile.qlink.ui.activity.eth.module.EthWalletDetailModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthWalletDetailActivity
 * @date 2018/10/25 15:02:11
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthWalletDetailModule.class)
public interface EthWalletDetailComponent {
    EthWalletDetailActivity inject(EthWalletDetailActivity Activity);
}