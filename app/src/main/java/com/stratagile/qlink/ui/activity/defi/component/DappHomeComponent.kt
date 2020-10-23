package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DappHomeFragment
import com.stratagile.qlink.ui.activity.defi.module.DappHomeModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DappHomeFragment
 * @date 2020/10/15 16:00:43
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DappHomeModule::class))
interface DappHomeComponent {
    fun inject(DappHomeFragment: DappHomeFragment): DappHomeFragment
}