package com.stratagile.qlink.ui.activity.reward.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.reward.MyClaimActivity
import com.stratagile.qlink.ui.activity.reward.module.MyClaimModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.claim
 * @Description: The component for MyClaimActivity
 * @date 2019/10/09 11:57:31
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MyClaimModule::class))
interface MyClaimComponent {
    fun inject(MyClaimActivity: MyClaimActivity): MyClaimActivity
}