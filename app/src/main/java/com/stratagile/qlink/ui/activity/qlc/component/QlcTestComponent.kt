package com.stratagile.qlink.ui.activity.qlc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.QlcTestActivity
import com.stratagile.qlink.ui.activity.qlc.module.QlcTestModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for QlcTestActivity
 * @date 2019/05/05 16:24:30
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(QlcTestModule::class))
interface QlcTestComponent {
    fun inject(QlcTestActivity: QlcTestActivity): QlcTestActivity
}