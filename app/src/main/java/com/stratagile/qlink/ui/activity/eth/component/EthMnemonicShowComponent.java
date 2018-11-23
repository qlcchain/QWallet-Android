package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicShowActivity;
import com.stratagile.qlink.ui.activity.eth.module.EthMnemonicShowModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthMnemonicShowActivity
 * @date 2018/10/23 15:34:11
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthMnemonicShowModule.class)
public interface EthMnemonicShowComponent {
    EthMnemonicShowActivity inject(EthMnemonicShowActivity Activity);
}