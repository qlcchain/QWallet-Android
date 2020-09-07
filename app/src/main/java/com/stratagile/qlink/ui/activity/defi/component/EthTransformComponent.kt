package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.EthTransformActivity
import com.stratagile.qlink.ui.activity.defi.module.EthTransformModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for EthTransformActivity
 * @date 2020/08/15 16:13:43
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EthTransformModule::class))
interface EthTransformComponent {
    fun inject(EthTransformActivity: EthTransformActivity): EthTransformActivity
}