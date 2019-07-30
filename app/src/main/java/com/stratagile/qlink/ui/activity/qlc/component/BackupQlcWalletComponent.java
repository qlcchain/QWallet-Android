package com.stratagile.qlink.ui.activity.qlc.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.BackupQlcWalletActivity;
import com.stratagile.qlink.ui.activity.qlc.module.BackupQlcWalletModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for BackupQlcWalletActivity
 * @date 2019/05/20 10:52:55
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = BackupQlcWalletModule.class)
public interface BackupQlcWalletComponent {
    BackupQlcWalletActivity inject(BackupQlcWalletActivity Activity);
}