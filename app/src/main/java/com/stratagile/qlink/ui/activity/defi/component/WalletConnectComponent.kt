package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.WalletConnectActivity
import com.stratagile.qlink.ui.activity.defi.module.WalletConnectModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for WalletConnectActivity
 * @date 2020/09/15 16:03:09
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(WalletConnectModule::class))
interface WalletConnectComponent {
    fun inject(WalletConnectActivity: WalletConnectActivity): WalletConnectActivity
}