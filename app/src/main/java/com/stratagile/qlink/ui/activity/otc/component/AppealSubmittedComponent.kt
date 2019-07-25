package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealSubmittedActivity
import com.stratagile.qlink.ui.activity.otc.module.AppealSubmittedModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for AppealSubmittedActivity
 * @date 2019/07/22 15:34:25
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(AppealSubmittedModule::class))
interface AppealSubmittedComponent {
    fun inject(AppealSubmittedActivity: AppealSubmittedActivity): AppealSubmittedActivity
}