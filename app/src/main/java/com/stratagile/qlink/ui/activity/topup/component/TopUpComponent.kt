package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopUpFragment
import com.stratagile.qlink.ui.activity.topup.module.TopUpModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopUpFragment
 * @date 2019/09/23 15:54:17
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopUpModule::class))
interface TopUpComponent {
    fun inject(TopUpFragment: TopUpFragment): TopUpFragment
}