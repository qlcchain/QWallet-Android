package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicFragment;
import com.stratagile.qlink.ui.activity.eth.module.EthMnemonicModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthMnemonicFragment
 * @date 2018/10/22 14:12:37
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthMnemonicModule.class)
public interface EthMnemonicComponent {
    EthMnemonicFragment inject(EthMnemonicFragment Fragment);
}