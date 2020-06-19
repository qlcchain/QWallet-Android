package com.stratagile.qlink.ui.activity.main.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.main.DefiFragment
import com.stratagile.qlink.ui.activity.main.module.DefiModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for DefiFragment
 * @date 2020/05/25 11:29:00
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiModule::class))
interface DefiComponent {
    fun inject(DefiFragment: DefiFragment): DefiFragment
}