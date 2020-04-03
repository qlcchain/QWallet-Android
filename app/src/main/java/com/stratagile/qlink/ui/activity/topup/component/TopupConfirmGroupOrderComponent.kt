package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupConfirmGroupOrderActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupConfirmGroupOrderModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupConfirmGroupOrderActivity
 * @date 2020/02/13 16:15:05
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupConfirmGroupOrderModule::class))
interface TopupConfirmGroupOrderComponent {
    fun inject(TopupConfirmGroupOrderActivity: TopupConfirmGroupOrderActivity): TopupConfirmGroupOrderActivity
}