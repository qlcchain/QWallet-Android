package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiListFragment
import com.stratagile.qlink.ui.activity.defi.module.DefiListModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiListFragment
 * @date 2020/05/25 17:10:05
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiListModule::class))
interface DefiListComponent {
    fun inject(DefiListFragment: DefiListFragment): DefiListFragment
}