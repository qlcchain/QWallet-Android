package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicClaimActivity
import com.stratagile.qlink.ui.activity.my.module.EpidemicClaimModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for EpidemicClaimActivity
 * @date 2020/04/15 17:22:27
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EpidemicClaimModule::class))
interface EpidemicClaimComponent {
    fun inject(EpidemicClaimActivity: EpidemicClaimActivity): EpidemicClaimActivity
}