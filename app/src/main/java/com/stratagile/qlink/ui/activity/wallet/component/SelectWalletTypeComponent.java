package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.ui.activity.wallet.module.SelectWalletTypeModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for SelectWalletTypeActivity
 * @date 2018/10/19 10:51:45
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SelectWalletTypeModule.class)
public interface SelectWalletTypeComponent {
    SelectWalletTypeActivity inject(SelectWalletTypeActivity Activity);
}