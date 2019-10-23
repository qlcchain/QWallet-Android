package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcQlcChainPayActivity
import com.stratagile.qlink.ui.activity.otc.module.OtcQlcChainPayModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OtcQlcChainPayActivity
 * @date 2019/08/23 12:01:58
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OtcQlcChainPayModule::class))
interface OtcQlcChainPayComponent {
    fun inject(OtcQlcChainPayActivity: OtcQlcChainPayActivity): OtcQlcChainPayActivity
}