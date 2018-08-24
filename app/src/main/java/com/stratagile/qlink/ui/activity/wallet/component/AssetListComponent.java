package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.AssetListFragment;
import com.stratagile.qlink.ui.activity.wallet.module.AssetListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for AssetListFragment
 * @date 2018/01/18 20:42:28
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = AssetListModule.class)
public interface AssetListComponent {
    AssetListFragment inject(AssetListFragment Fragment);
}