package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcNeoChainPayActivity
import com.stratagile.qlink.ui.activity.otc.module.OtcNeoChainPayModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OtcNeoChainPayActivity
 * @date 2019/08/21 15:07:05
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OtcNeoChainPayModule::class))
interface OtcNeoChainPayComponent {
    fun inject(OtcNeoChainPayActivity: OtcNeoChainPayActivity): OtcNeoChainPayActivity
}