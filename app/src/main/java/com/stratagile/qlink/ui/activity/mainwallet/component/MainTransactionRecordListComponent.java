package com.stratagile.qlink.ui.activity.mainwallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainTransactionRecordListFragment;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainTransactionRecordListModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The component for MainTransactionRecordListFragment
 * @date 2018/06/13 20:52:12
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainTransactionRecordListModule.class)
public interface MainTransactionRecordListComponent {
    MainTransactionRecordListFragment inject(MainTransactionRecordListFragment Fragment);
}