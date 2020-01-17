package com.stratagile.qlink.ui.activity.recommend.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.GroupExplainActivity
import com.stratagile.qlink.ui.activity.recommend.module.GroupExplainModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The component for GroupExplainActivity
 * @date 2020/01/17 13:37:58
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(GroupExplainModule::class))
interface GroupExplainComponent {
    fun inject(GroupExplainActivity: GroupExplainActivity): GroupExplainActivity
}