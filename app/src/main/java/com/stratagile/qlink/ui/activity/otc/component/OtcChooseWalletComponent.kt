package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.otc.module.OtcChooseWalletModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OtcChooseWalletActivity
 * @date 2019/07/29 11:54:14
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OtcChooseWalletModule::class))
interface OtcChooseWalletComponent {
    fun inject(OtcChooseWalletActivity: OtcChooseWalletActivity): OtcChooseWalletActivity
}