package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiKeyStateFragment
import com.stratagile.qlink.ui.activity.defi.module.DefiKeyStateModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiKeyStateFragment
 * @date 2020/06/02 10:25:39
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiKeyStateModule::class))
interface DefiKeyStateComponent {
    fun inject(DefiKeyStateFragment: DefiKeyStateFragment): DefiKeyStateFragment
}