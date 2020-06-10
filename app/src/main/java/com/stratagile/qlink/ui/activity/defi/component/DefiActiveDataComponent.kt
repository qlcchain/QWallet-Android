package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiActiveDataFragment
import com.stratagile.qlink.ui.activity.defi.module.DefiActiveDataModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for DefiActiveDataFragment
 * @date 2020/06/02 10:25:59
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(DefiActiveDataModule::class))
interface DefiActiveDataComponent {
    fun inject(DefiActiveDataFragment: DefiActiveDataFragment): DefiActiveDataFragment
}