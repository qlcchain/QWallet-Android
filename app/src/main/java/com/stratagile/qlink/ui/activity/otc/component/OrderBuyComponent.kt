package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderBuyFragment
import com.stratagile.qlink.ui.activity.otc.module.OrderBuyModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OrderBuyFragment
 * @date 2019/07/08 17:24:58
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OrderBuyModule::class))
interface OrderBuyComponent {
    fun inject(OrderBuyFragment: OrderBuyFragment): OrderBuyFragment
}