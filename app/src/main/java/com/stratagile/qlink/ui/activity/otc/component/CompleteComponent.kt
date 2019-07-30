package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.CompleteFragment
import com.stratagile.qlink.ui.activity.otc.module.CompleteModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for CompleteFragment
 * @date 2019/07/16 17:53:03
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(CompleteModule::class))
interface CompleteComponent {
    fun inject(CompleteFragment: CompleteFragment): CompleteFragment
}