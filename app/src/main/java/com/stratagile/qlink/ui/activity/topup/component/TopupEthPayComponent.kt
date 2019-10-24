package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupEthPayActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupEthPayModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupEthPayActivity
 * @date 2019/10/24 10:18:43
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupEthPayModule::class))
interface TopupEthPayComponent {
    fun inject(TopupEthPayActivity: TopupEthPayActivity): TopupEthPayActivity
}