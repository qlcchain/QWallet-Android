package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiDetailActivity
import com.stratagile.qlink.ui.activity.defi.module.DefiDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiDetailActivity
 * @date 2020/05/29 09:13:19
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiDetailModule::class))
interface DefiDetailComponent {
    fun inject(DefiDetailActivity: DefiDetailActivity): DefiDetailActivity
}