package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderRecordActivity
import com.stratagile.qlink.ui.activity.otc.module.OrderRecordModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OrderRecordActivity
 * @date 2019/07/10 14:40:52
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OrderRecordModule::class))
interface OrderRecordComponent {
    fun inject(OrderRecordActivity: OrderRecordActivity): OrderRecordActivity
}