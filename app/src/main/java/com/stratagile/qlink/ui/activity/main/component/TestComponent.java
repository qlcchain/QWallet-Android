package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.TestActivity;
import com.stratagile.qlink.ui.activity.main.module.TestModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for TestActivity
 * @date 2018/12/18 11:09:36
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = TestModule.class)
public interface TestComponent {
    TestActivity inject(TestActivity Activity);
}