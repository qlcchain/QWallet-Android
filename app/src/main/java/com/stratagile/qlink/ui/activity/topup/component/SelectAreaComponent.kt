package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.SelectAreaActivity
import com.stratagile.qlink.ui.activity.topup.module.SelectAreaModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for SelectAreaActivity
 * @date 2019/09/24 16:07:07
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SelectAreaModule::class))
interface SelectAreaComponent {
    fun inject(SelectAreaActivity: SelectAreaActivity): SelectAreaActivity
}