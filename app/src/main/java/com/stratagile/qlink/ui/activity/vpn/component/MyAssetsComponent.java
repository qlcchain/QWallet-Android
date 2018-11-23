package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.MyAssetsActivity;
import com.stratagile.qlink.ui.activity.vpn.module.MyAssetsModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for MyAssetsActivity
 * @date 2018/11/11 21:42:25
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MyAssetsModule.class)
public interface MyAssetsComponent {
    MyAssetsActivity inject(MyAssetsActivity Activity);
}