package com.stratagile.qlink.ui.activity.mainwallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainSendFragment;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainSendModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The component for MainSendFragment
 * @date 2018/06/14 09:24:50
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainSendModule.class)
public interface MainSendComponent {
    MainSendFragment inject(MainSendFragment Fragment);
}