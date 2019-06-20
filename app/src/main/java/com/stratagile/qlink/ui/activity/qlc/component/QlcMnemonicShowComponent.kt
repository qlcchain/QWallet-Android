package com.stratagile.qlink.ui.activity.qlc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicShowActivity
import com.stratagile.qlink.ui.activity.qlc.module.QlcMnemonicShowModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for QlcMnemonicShowActivity
 * @date 2019/06/05 18:36:43
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(QlcMnemonicShowModule::class))
interface QlcMnemonicShowComponent {
    fun inject(QlcMnemonicShowActivity: QlcMnemonicShowActivity): QlcMnemonicShowActivity
}