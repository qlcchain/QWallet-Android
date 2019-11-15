package com.stratagile.qlink.ui.activity.mining.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.mining.MiningRewardActivity
import com.stratagile.qlink.ui.activity.mining.module.MiningRewardModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: The component for MiningRewardActivity
 * @date 2019/11/15 15:49:47
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MiningRewardModule::class))
interface MiningRewardComponent {
    fun inject(MiningRewardActivity: MiningRewardActivity): MiningRewardActivity
}