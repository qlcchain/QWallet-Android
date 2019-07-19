package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealActivity
import com.stratagile.qlink.ui.activity.otc.module.AppealModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for AppealActivity
 * @date 2019/07/19 11:44:36
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(AppealModule::class))
interface AppealComponent {
    fun inject(AppealActivity: AppealActivity): AppealActivity
}