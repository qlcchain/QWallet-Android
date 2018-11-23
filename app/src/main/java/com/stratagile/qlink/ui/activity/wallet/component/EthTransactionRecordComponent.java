package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.EthTransactionRecordActivity;
import com.stratagile.qlink.ui.activity.wallet.module.EthTransactionRecordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for EthTransactionRecordActivity
 * @date 2018/10/29 16:12:21
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthTransactionRecordModule.class)
public interface EthTransactionRecordComponent {
    EthTransactionRecordActivity inject(EthTransactionRecordActivity Activity);
}