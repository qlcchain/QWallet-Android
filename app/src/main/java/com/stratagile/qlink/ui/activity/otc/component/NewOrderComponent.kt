package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.NewOrderActivity
import com.stratagile.qlink.ui.activity.otc.module.NewOrderModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for NewOrderActivity
 * @date 2019/07/08 16:00:52
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(NewOrderModule::class))
interface NewOrderComponent {
    fun inject(NewOrderActivity: NewOrderActivity): NewOrderActivity
}