package com.stratagile.qlink.ui.activity.eth.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.EthKeyStroeFragment;
import com.stratagile.qlink.ui.activity.eth.module.EthKeyStroeModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The component for EthKeyStroeFragment
 * @date 2018/05/24 17:48:34
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EthKeyStroeModule.class)
public interface EthKeyStroeComponent {
    EthKeyStroeFragment inject(EthKeyStroeFragment Fragment);
}