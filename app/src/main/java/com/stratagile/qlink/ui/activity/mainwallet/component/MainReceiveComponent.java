package com.stratagile.qlink.ui.activity.mainwallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainReceiveFragment;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainReceiveModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The component for MainReceiveFragment
 * @date 2018/06/14 09:25:13
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainReceiveModule.class)
public interface MainReceiveComponent {
    MainReceiveFragment inject(MainReceiveFragment Fragment);
}