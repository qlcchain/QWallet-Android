package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.LogActivity;
import com.stratagile.qlink.ui.activity.main.module.LogModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for LogActivity
 * @date 2018/02/12 14:51:34
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = LogModule.class)
public interface LogComponent {
    LogActivity inject(LogActivity Activity);
}