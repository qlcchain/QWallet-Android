package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupDeductionEthChainActivity
import com.stratagile.qlink.ui.activity.topup.module.TopupDeductionEthChainModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for TopupDeductionEthChainActivity
 * @date 2019/12/27 11:59:29
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TopupDeductionEthChainModule::class))
interface TopupDeductionEthChainComponent {
    fun inject(TopupDeductionEthChainActivity: TopupDeductionEthChainActivity): TopupDeductionEthChainActivity
}