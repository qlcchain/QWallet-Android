package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.EthSwapFragment
import com.stratagile.qlink.ui.activity.defi.module.EthSwapModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for EthSwapFragment
 * @date 2020/08/15 16:40:33
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EthSwapModule::class))
interface EthSwapComponent {
    fun inject(EthSwapFragment: EthSwapFragment): EthSwapFragment
}