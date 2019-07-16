package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealFragment
import com.stratagile.qlink.ui.activity.otc.module.AppealModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for AppealFragment
 * @date 2019/07/16 17:53:24
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(AppealModule::class))
interface AppealComponent {
    fun inject(AppealFragment: AppealFragment): AppealFragment
}