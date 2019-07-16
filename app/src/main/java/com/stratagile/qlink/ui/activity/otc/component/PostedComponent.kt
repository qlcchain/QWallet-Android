package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.PostedFragment
import com.stratagile.qlink.ui.activity.otc.module.PostedModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for PostedFragment
 * @date 2019/07/16 17:52:28
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(PostedModule::class))
interface PostedComponent {
    fun inject(PostedFragment: PostedFragment): PostedFragment
}