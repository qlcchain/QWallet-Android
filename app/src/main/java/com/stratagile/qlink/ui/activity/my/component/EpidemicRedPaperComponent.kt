package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicRedPaperActivity
import com.stratagile.qlink.ui.activity.my.module.EpidemicRedPaperModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for EpidemicRedPaperActivity
 * @date 2020/04/13 17:05:33
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EpidemicRedPaperModule::class))
interface EpidemicRedPaperComponent {
    fun inject(EpidemicRedPaperActivity: EpidemicRedPaperActivity): EpidemicRedPaperActivity
}