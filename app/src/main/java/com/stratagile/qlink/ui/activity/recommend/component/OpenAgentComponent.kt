package com.stratagile.qlink.ui.activity.recommend.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.OpenAgentActivity
import com.stratagile.qlink.ui.activity.recommend.module.OpenAgentModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The component for OpenAgentActivity
 * @date 2020/01/09 13:59:03
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OpenAgentModule::class))
interface OpenAgentComponent {
    fun inject(OpenAgentActivity: OpenAgentActivity): OpenAgentActivity
}