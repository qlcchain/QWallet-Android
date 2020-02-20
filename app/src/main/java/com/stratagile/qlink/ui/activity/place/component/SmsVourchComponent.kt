package com.stratagile.qlink.ui.activity.place.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.place.SmsVourchActivity
import com.stratagile.qlink.ui.activity.place.module.SmsVourchModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: The component for SmsVourchActivity
 * @date 2020/02/20 22:53:22
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SmsVourchModule::class))
interface SmsVourchComponent {
    fun inject(SmsVourchActivity: SmsVourchActivity): SmsVourchActivity
}