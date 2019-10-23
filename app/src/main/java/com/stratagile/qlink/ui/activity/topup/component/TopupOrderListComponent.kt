package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupOrderListActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupOrderListModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupOrderListActivity
 * @date 2019/09/26 15:00:15
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupOrderListModule::class))
interface TopupOrderListComponent {
    fun inject(TopupOrderListActivity: TopupOrderListActivity): TopupOrderListActivity
}