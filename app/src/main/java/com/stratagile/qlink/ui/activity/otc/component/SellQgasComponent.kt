package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.SellQgasActivity
import com.stratagile.qlink.ui.activity.otc.module.SellQgasModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for SellQgasActivity
 * @date 2019/07/09 14:18:11
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SellQgasModule::class))
interface SellQgasComponent {
    fun inject(SellQgasActivity: SellQgasActivity): SellQgasActivity
}