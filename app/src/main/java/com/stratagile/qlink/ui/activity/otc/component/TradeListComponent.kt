package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.TradeListFragment
import com.stratagile.qlink.ui.activity.otc.module.TradeListModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for TradeListFragment
 * @date 2019/08/19 09:38:46
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TradeListModule::class))
interface TradeListComponent {
    fun inject(TradeListFragment: TradeListFragment): TradeListFragment
}