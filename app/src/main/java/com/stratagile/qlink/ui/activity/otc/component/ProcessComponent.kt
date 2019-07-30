package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.ProcessFragment
import com.stratagile.qlink.ui.activity.otc.module.ProcessModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for ProcessFragment
 * @date 2019/07/16 17:52:45
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ProcessModule::class))
interface ProcessComponent {
    fun inject(ProcessFragment: ProcessFragment): ProcessFragment
}