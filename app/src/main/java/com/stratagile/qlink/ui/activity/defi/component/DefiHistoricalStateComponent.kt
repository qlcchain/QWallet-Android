package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiHistoricalStateFragment
import com.stratagile.qlink.ui.activity.defi.module.DefiHistoricalStateModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiHistoricalStateFragment
 * @date 2020/06/02 10:26:24
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiHistoricalStateModule::class))
interface DefiHistoricalStateComponent {
    fun inject(DefiHistoricalStateFragment: DefiHistoricalStateFragment): DefiHistoricalStateFragment
}