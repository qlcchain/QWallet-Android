package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupDeductionQlcChainActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupDeductionQlcChainModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupDeductionQlcChainActivity
 * @date 2019/12/26 14:45:12
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupDeductionQlcChainModule::class))
interface TopupDeductionQlcChainComponent {
    fun inject(TopupDeductionQlcChainActivity: TopupDeductionQlcChainActivity): TopupDeductionQlcChainActivity
}