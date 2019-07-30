package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.ClosedOrderFragment
import com.stratagile.qlink.ui.activity.otc.module.CloasedOrderModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for CloasedOrderFragment
 * @date 2019/07/17 14:39:46
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(CloasedOrderModule::class))
interface CloasedOrderComponent {
    fun inject(CloasedOrderFragment: ClosedOrderFragment): ClosedOrderFragment
}