package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.BurnIntroduceActivity
import com.stratagile.qlink.ui.activity.my.module.BurnIntroduceModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for BurnIntroduceActivity
 * @date 2020/02/29 17:33:46
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(BurnIntroduceModule::class))
interface BurnIntroduceComponent {
    fun inject(BurnIntroduceActivity: BurnIntroduceActivity): BurnIntroduceActivity
}