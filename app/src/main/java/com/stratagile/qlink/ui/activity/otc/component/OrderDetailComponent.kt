package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderDetailActivity
import com.stratagile.qlink.ui.activity.otc.module.OrderDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OrderDetailActivity
 * @date 2019/07/10 10:03:30
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OrderDetailModule::class))
interface OrderDetailComponent {
    fun inject(OrderDetailActivity: OrderDetailActivity): OrderDetailActivity
}