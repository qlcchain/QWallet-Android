package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SwapFragment
import com.stratagile.qlink.ui.activity.defi.module.SwapModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for SwapFragment
 * @date 2020/08/12 15:49:07
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SwapModule::class))
interface SwapComponent {
    fun inject(SwapFragment: SwapFragment): SwapFragment
}