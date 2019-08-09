package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.ConfidantFragment
import com.stratagile.qlink.ui.activity.stake.module.ConfidantModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for ConfidantFragment
 * @date 2019/08/08 16:38:01
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ConfidantModule::class))
interface ConfidantComponent {
    fun inject(ConfidantFragment: ConfidantFragment): ConfidantFragment
}