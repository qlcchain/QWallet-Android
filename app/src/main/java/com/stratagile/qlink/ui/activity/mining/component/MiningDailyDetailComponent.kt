package com.stratagile.qlink.ui.activity.mining.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.mining.MiningDailyDetailActivity
import com.stratagile.qlink.ui.activity.mining.module.MiningDailyDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: The component for MiningDailyDetailActivity
 * @date 2019/11/14 18:13:10
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MiningDailyDetailModule::class))
interface MiningDailyDetailComponent {
    fun inject(MiningDailyDetailActivity: MiningDailyDetailActivity): MiningDailyDetailActivity
}