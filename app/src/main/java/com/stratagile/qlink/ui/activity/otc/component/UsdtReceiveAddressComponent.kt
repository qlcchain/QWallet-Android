package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.UsdtReceiveAddressActivity
import com.stratagile.qlink.ui.activity.otc.module.UsdtReceiveAddressModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for UsdtReceiveAddressActivity
 * @date 2019/07/17 16:50:34
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(UsdtReceiveAddressModule::class))
interface UsdtReceiveAddressComponent {
    fun inject(UsdtReceiveAddressActivity: UsdtReceiveAddressActivity): UsdtReceiveAddressActivity
}