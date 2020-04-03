package com.stratagile.qlink.ui.activity.my.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.VoteActivity
import com.stratagile.qlink.ui.activity.my.module.VoteModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for VoteActivity
 * @date 2020/02/26 10:34:02
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(VoteModule::class))
interface VoteComponent {
    fun inject(VoteActivity: VoteActivity): VoteActivity
}