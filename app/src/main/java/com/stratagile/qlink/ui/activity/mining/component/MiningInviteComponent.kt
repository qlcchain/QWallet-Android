package com.stratagile.qlink.ui.activity.mining.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity
import com.stratagile.qlink.ui.activity.mining.module.MiningInviteModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: The component for MiningInviteActivity
 * @date 2019/11/14 09:43:06
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MiningInviteModule::class))
interface MiningInviteComponent {
    fun inject(MiningInviteActivity: MiningInviteActivity): MiningInviteActivity
}