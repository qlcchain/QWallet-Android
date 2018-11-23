package com.stratagile.qlink.ui.activity.test.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.test.TestFragment
import com.stratagile.qlink.ui.activity.test.module.TestModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.test
 * @Description: The component for TestFragment
 * @date 2018/09/10 16:51:54
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TestModule::class))
interface TestComponent {
    fun inject(Fragment: TestFragment): TestFragment
}