package com.stratagile.qlink.ui.activity.recommend.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.RecommendRewardActivity
import com.stratagile.qlink.ui.activity.recommend.module.RecommendRewardModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The component for RecommendRewardActivity
 * @date 2020/01/09 13:57:40
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(RecommendRewardModule::class))
interface RecommendRewardComponent {
    fun inject(RecommendRewardActivity: RecommendRewardActivity): RecommendRewardActivity
}