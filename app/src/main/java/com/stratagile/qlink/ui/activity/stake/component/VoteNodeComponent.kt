package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.VoteNodeFragment
import com.stratagile.qlink.ui.activity.stake.module.VoteNodeModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for VoteNodeFragment
 * @date 2019/08/08 16:37:41
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(VoteNodeModule::class))
interface VoteNodeComponent {
    fun inject(VoteNodeFragment: VoteNodeFragment): VoteNodeFragment
}