package com.stratagile.qlink.ui.activity.qlc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicbackupActivity
import com.stratagile.qlink.ui.activity.qlc.module.QlcMnemonicbackupModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for QlcMnemonicbackupActivity
 * @date 2019/06/05 18:37:03
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(QlcMnemonicbackupModule::class))
interface QlcMnemonicbackupComponent {
    fun inject(QlcMnemonicbackupActivity: QlcMnemonicbackupActivity): QlcMnemonicbackupActivity
}