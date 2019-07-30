package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.BuyQgasActivity
import com.stratagile.qlink.ui.activity.otc.module.BuyQgasModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for BuyQgasActivity
 * @date 2019/07/09 14:18:25
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(BuyQgasModule::class))
interface BuyQgasComponent {
    fun inject(BuyQgasActivity: BuyQgasActivity): BuyQgasActivity
}