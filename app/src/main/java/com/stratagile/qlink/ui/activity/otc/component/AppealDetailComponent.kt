package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealDetailActivity
import com.stratagile.qlink.ui.activity.otc.module.AppealDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for AppealDetailActivity
 * @date 2019/07/22 10:13:43
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(AppealDetailModule::class))
interface AppealDetailComponent {
    fun inject(AppealDetailActivity: AppealDetailActivity): AppealDetailActivity
}