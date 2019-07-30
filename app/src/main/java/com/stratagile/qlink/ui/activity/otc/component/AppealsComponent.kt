package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealsFragment
import com.stratagile.qlink.ui.activity.otc.module.AppealsModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for AppealsFragment
 * @date 2019/07/19 11:45:27
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(AppealsModule::class))
interface AppealsComponent {
    fun inject(AppealsFragment: AppealsFragment): AppealsFragment
}