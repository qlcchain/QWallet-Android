package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupQlcPayActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupQlcPayModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupQlcPayActivity
 * @date 2019/09/26 10:08:40
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupQlcPayModule::class))
interface TopupQlcPayComponent {
    fun inject(TopupQlcPayActivity: TopupQlcPayActivity): TopupQlcPayActivity
}