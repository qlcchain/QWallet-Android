package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.SelectOperatorActivity
import com.stratagile.qlink.ui.activity.topup.module.SelectOperatorModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for SelectOperatorActivity
 * @date 2019/12/25 10:21:32
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SelectOperatorModule::class))
interface SelectOperatorComponent {
    fun inject(SelectOperatorActivity: SelectOperatorActivity): SelectOperatorActivity
}