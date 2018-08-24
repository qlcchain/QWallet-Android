package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.ImportEthWalletActivity;
import com.stratagile.qlink.ui.activity.eth.module.ImportEthWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for ImportEthWalletActivity
 * @date 2018/05/24 16:30:41
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ImportEthWalletModule.class)
public interface ImportEthWalletComponent {
    ImportEthWalletActivity inject(ImportEthWalletActivity Activity);
}