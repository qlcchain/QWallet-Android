package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupPayNeoChainActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupPayNeoChainModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupPayNeoChainActivity
 * @date 2019/12/26 16:34:35
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupPayNeoChainModule::class))
interface TopupPayNeoChainComponent {
    fun inject(TopupPayNeoChainActivity: TopupPayNeoChainActivity): TopupPayNeoChainActivity
}