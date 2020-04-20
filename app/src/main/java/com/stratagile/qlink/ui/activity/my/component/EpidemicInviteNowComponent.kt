package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicInviteNowActivity
import com.stratagile.qlink.ui.activity.my.module.EpidemicInviteNowModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for EpidemicInviteNowActivity
 * @date 2020/04/17 10:25:30
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EpidemicInviteNowModule::class))
interface EpidemicInviteNowComponent {
    fun inject(EpidemicInviteNowActivity: EpidemicInviteNowActivity): EpidemicInviteNowActivity
}