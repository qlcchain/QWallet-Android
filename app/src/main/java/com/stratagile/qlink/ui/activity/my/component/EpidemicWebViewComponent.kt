package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicWebViewActivity
import com.stratagile.qlink.ui.activity.my.module.EpidemicWebViewModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for EpidemicWebViewActivity
 * @date 2020/04/16 17:48:35
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EpidemicWebViewModule::class))
interface EpidemicWebViewComponent {
    fun inject(EpidemicWebViewActivity: EpidemicWebViewActivity): EpidemicWebViewActivity
}