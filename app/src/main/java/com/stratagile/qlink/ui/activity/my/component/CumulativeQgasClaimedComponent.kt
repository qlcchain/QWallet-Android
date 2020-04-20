package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.CumulativeQgasClaimedActivity
import com.stratagile.qlink.ui.activity.my.module.CumulativeQgasClaimedModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for CumulativeQgasClaimedActivity
 * @date 2020/04/15 09:37:37
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(CumulativeQgasClaimedModule::class))
interface CumulativeQgasClaimedComponent {
    fun inject(CumulativeQgasClaimedActivity: CumulativeQgasClaimedActivity): CumulativeQgasClaimedActivity
}