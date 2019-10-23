package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.StakeDetailActivity
import com.stratagile.qlink.ui.activity.stake.module.StakeDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for StakeDetailActivity
 * @date 2019/08/09 15:26:02
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(StakeDetailModule::class))
interface StakeDetailComponent {
    fun inject(StakeDetailActivity: StakeDetailActivity): StakeDetailActivity
}