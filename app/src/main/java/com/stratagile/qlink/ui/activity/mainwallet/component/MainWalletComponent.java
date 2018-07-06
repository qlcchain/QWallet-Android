package com.stratagile.qlink.ui.activity.mainwallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainWalletActivity;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainWalletModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The component for MainWalletActivity
 * @date 2018/06/13 14:09:33
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainWalletModule.class)
public interface MainWalletComponent {
    MainWalletActivity inject(MainWalletActivity Activity);
}