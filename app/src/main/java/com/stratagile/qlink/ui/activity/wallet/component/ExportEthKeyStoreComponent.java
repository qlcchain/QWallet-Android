package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ExportEthKeyStoreActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ExportEthKeyStoreModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ExportEthKeyStoreActivity
 * @date 2018/11/06 17:23:06
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ExportEthKeyStoreModule.class)
public interface ExportEthKeyStoreComponent {
    ExportEthKeyStoreActivity inject(ExportEthKeyStoreActivity Activity);
}