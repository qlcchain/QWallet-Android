package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiNewsFragment
import com.stratagile.qlink.ui.activity.defi.module.DefiNewsModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiNewsFragment
 * @date 2020/05/25 17:10:21
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiNewsModule::class))
interface DefiNewsComponent {
    fun inject(DefiNewsFragment: DefiNewsFragment): DefiNewsFragment
}