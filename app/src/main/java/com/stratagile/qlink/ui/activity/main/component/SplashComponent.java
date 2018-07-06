package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.SplashActivity;
import com.stratagile.qlink.ui.activity.main.module.SplashModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for SplashActivity
 * @date 2018/01/09 11:24:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SplashModule.class)
public interface SplashComponent {
    SplashActivity inject(SplashActivity Activity);
}