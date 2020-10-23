package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DappBrowserActivity
import com.stratagile.qlink.ui.activity.defi.module.DappBrowserModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DappBrowserActivity
 * @date 2020/10/15 17:56:57
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DappBrowserModule::class))
interface DappBrowserComponent {
    fun inject(DappBrowserActivity: DappBrowserActivity): DappBrowserActivity
}