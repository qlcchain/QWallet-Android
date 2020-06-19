package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiNewsDetailActivity
import com.stratagile.qlink.ui.activity.defi.module.DefiNewsDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiNewsDetailActivity
 * @date 2020/06/05 14:09:12
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiNewsDetailModule::class))
interface DefiNewsDetailComponent {
    fun inject(DefiNewsDetailActivity: DefiNewsDetailActivity): DefiNewsDetailActivity
}