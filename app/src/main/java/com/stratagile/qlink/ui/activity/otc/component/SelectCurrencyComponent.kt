package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.SelectCurrencyActivity
import com.stratagile.qlink.ui.activity.otc.module.SelectCurrencyModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for SelectCurrencyActivity
 * @date 2019/08/19 15:22:57
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SelectCurrencyModule::class))
interface SelectCurrencyComponent {
    fun inject(SelectCurrencyActivity: SelectCurrencyActivity): SelectCurrencyActivity
}