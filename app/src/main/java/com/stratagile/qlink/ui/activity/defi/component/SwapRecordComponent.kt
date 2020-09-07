package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SwapRecordFragment
import com.stratagile.qlink.ui.activity.defi.module.SwapRecordModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for SwapRecordFragment
 * @date 2020/08/12 15:49:25
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SwapRecordModule::class))
interface SwapRecordComponent {
    fun inject(SwapRecordFragment: SwapRecordFragment): SwapRecordFragment
}