package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.TradeOrderDetailActivity
import com.stratagile.qlink.ui.activity.otc.module.TradeOrderDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for TradeOrderDetailActivity
 * @date 2019/07/17 11:14:13
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TradeOrderDetailModule::class))
interface TradeOrderDetailComponent {
    fun inject(TradeOrderDetailActivity: TradeOrderDetailActivity): TradeOrderDetailActivity
}