package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.TransactionRecordActivity;
import com.stratagile.qlink.ui.activity.wallet.module.TransactionRecordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for TransactionRecordActivity
 * @date 2018/01/26 17:16:57
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = TransactionRecordModule.class)
public interface TransactionRecordComponent {
    TransactionRecordActivity inject(TransactionRecordActivity Activity);
}