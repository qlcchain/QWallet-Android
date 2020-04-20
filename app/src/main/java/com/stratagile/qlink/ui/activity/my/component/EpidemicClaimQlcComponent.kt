package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicClaimQlcActivity
import com.stratagile.qlink.ui.activity.my.module.EpidemicClaimQlcModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for EpidemicClaimQlcActivity
 * @date 2020/04/16 15:57:15
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EpidemicClaimQlcModule::class))
interface EpidemicClaimQlcComponent {
    fun inject(EpidemicClaimQlcActivity: EpidemicClaimQlcActivity): EpidemicClaimQlcActivity
}