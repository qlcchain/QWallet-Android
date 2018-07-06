package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthPrivateKeyFragment;
import com.stratagile.qlink.ui.activity.eth.module.EthPrivateKeyModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthPrivateKeyFragment
 * @date 2018/05/24 17:49:02
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthPrivateKeyModule.class)
public interface EthPrivateKeyComponent {
    EthPrivateKeyFragment inject(EthPrivateKeyFragment Fragment);
}