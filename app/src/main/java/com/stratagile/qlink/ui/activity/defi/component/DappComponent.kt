package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DappFragment
import com.stratagile.qlink.ui.activity.defi.module.DappModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DappFragment
 * @date 2020/09/17 10:44:40
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DappModule::class))
interface DappComponent {
    fun inject(DappFragment: DappFragment): DappFragment
}