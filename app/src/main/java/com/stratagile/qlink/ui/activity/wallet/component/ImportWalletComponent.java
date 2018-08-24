package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ImportWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ImportWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ImportWalletActivity
 * @date 2018/01/23 14:24:49
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ImportWalletModule.class)
public interface ImportWalletComponent {
    ImportWalletActivity inject(ImportWalletActivity Activity);
}