package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.ConfirmActivity
import com.stratagile.qlink.ui.activity.defi.module.ConfirmModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for ConfirmActivity
 * @date 2020/10/14 16:59:28
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ConfirmModule::class))
interface ConfirmComponent {
    fun inject(ConfirmActivity: ConfirmActivity): ConfirmActivity
}