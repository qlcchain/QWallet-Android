package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderSellFragment
import com.stratagile.qlink.ui.activity.otc.module.OrderSellModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OrderSellFragment
 * @date 2019/07/08 17:24:46
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OrderSellModule::class))
interface OrderSellComponent {
    fun inject(OrderSellFragment: OrderSellFragment): OrderSellFragment
}