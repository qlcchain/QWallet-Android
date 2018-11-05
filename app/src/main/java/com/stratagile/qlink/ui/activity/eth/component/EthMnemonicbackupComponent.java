package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicbackupActivity;
import com.stratagile.qlink.ui.activity.eth.module.EthMnemonicbackupModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthMnemonicbackupActivity
 * @date 2018/10/23 15:13:44
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthMnemonicbackupModule.class)
public interface EthMnemonicbackupComponent {
    EthMnemonicbackupActivity inject(EthMnemonicbackupActivity Activity);
}