package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SwapDetailActivity
import com.stratagile.qlink.ui.activity.defi.module.SwapDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for SwapDetailActivity
 * @date 2020/08/25 11:39:23
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SwapDetailModule::class))
interface SwapDetailComponent {
    fun inject(SwapDetailActivity: SwapDetailActivity): SwapDetailActivity
}