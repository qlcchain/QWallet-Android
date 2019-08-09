package com.stratagile.qlink.ui.activity.stake.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.NewStakeActivity
import com.stratagile.qlink.ui.activity.stake.module.NewStakeModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The component for NewStakeActivity
 * @date 2019/08/08 16:33:44
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(NewStakeModule::class))
interface NewStakeComponent {
    fun inject(NewStakeActivity: NewStakeActivity): NewStakeActivity
}