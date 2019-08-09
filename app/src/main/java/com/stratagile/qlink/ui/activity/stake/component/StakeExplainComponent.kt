package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.StakeExplainActivity
import com.stratagile.qlink.ui.activity.stake.module.StakeExplainModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for StakeExplainActivity
 * @date 2019/08/09 15:25:43
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(StakeExplainModule::class))
interface StakeExplainComponent {
    fun inject(StakeExplainActivity: StakeExplainActivity): StakeExplainActivity
}