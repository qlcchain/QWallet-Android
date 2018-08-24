package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.FreeConnectActivity;
import com.stratagile.qlink.ui.activity.wallet.module.FreeConnectModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for FreeConnectActivity
 * @date 2018/07/18 11:53:01
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = FreeConnectModule.class)
public interface FreeConnectComponent {
    FreeConnectActivity inject(FreeConnectActivity Activity);
}