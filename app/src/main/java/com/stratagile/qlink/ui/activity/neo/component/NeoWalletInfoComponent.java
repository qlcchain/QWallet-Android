package com.stratagile.qlink.ui.activity.neo.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.neo.NeoWalletInfoActivity;
import com.stratagile.qlink.ui.activity.neo.module.NeoWalletInfoModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for NeoWalletInfoActivity
 * @date 2018/11/05 17:19:51
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = NeoWalletInfoModule.class)
public interface NeoWalletInfoComponent {
    NeoWalletInfoActivity inject(NeoWalletInfoActivity Activity);
}