package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.UsdtPayActivity
import com.stratagile.qlink.ui.activity.otc.module.UsdtPayModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for UsdtPayActivity
 * @date 2019/07/31 13:58:11
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(UsdtPayModule::class))
interface UsdtPayComponent {
    fun inject(UsdtPayActivity: UsdtPayActivity): UsdtPayActivity
}