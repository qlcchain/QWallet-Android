package com.stratagile.qlink.ui.activity.recommend.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.MyTopupGroupActivity
import com.stratagile.qlink.ui.activity.recommend.module.MyTopupGroupModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The component for MyTopupGroupActivity
 * @date 2020/01/15 16:21:51
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(MyTopupGroupModule::class))
interface MyTopupGroupComponent {
    fun inject(MyTopupGroupActivity: MyTopupGroupActivity): MyTopupGroupActivity
}