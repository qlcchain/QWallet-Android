package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupSelectDeductionTokenActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupSelectDeductionTokenModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupSelectDeductionTokenActivity
 * @date 2020/02/13 20:41:09
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupSelectDeductionTokenModule::class))
interface TopupSelectDeductionTokenComponent {
    fun inject(TopupSelectDeductionTokenActivity: TopupSelectDeductionTokenActivity): TopupSelectDeductionTokenActivity
}