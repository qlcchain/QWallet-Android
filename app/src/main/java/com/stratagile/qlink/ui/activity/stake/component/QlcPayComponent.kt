package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.QlcPayActivity
import com.stratagile.qlink.ui.activity.stake.module.QlcPayModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for QlcPayActivity
 * @date 2019/08/16 09:59:21
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(QlcPayModule::class))
interface QlcPayComponent {
    fun inject(QlcPayActivity: QlcPayActivity): QlcPayActivity
}