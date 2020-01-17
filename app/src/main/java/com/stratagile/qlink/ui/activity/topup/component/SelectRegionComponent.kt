package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.SelectRegionActivity
import com.stratagile.qlink.ui.activity.topup.module.SelectRegionModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for SelectRegionActivity
 * @date 2019/12/25 14:50:07
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SelectRegionModule::class))
interface SelectRegionComponent {
    fun inject(SelectRegionActivity: SelectRegionActivity): SelectRegionActivity
}