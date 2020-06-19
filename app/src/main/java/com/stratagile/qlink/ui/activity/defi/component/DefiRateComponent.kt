package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiRateActivity
import com.stratagile.qlink.ui.activity.defi.module.DefiRateModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiRateActivity
 * @date 2020/06/03 14:17:18
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiRateModule::class))
interface DefiRateComponent {
    fun inject(DefiRateActivity: DefiRateActivity): DefiRateActivity
}