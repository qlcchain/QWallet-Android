package com.stratagile.qlink.ui.activity.neo.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.neo.NeoTestActivity
import com.stratagile.qlink.ui.activity.neo.module.NeoTestModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.neo
 * @Description: The component for NeoTestActivity
 * @date 2019/06/10 10:30:03
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(NeoTestModule::class))
interface NeoTestComponent {
    fun inject(NeoTestActivity: NeoTestActivity): NeoTestActivity
}