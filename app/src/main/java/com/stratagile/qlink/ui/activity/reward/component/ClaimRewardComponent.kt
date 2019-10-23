package com.stratagile.qlink.ui.activity.reward.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.reward.ClaimRewardActivity
import com.stratagile.qlink.ui.activity.reward.module.ClaimRewardModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.reward
 * @Description: The component for ClaimRewardActivity
 * @date 2019/10/10 15:28:24
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ClaimRewardModule::class))
interface ClaimRewardComponent {
    fun inject(ClaimRewardActivity: ClaimRewardActivity): ClaimRewardActivity
}