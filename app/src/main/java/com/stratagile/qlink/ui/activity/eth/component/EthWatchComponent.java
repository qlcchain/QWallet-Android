package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthWatchFragment;
import com.stratagile.qlink.ui.activity.eth.module.EthWatchModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthWatchFragment
 * @date 2018/10/22 14:16:26
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthWatchModule.class)
public interface EthWatchComponent {
    EthWatchFragment inject(EthWatchFragment Fragment);
}