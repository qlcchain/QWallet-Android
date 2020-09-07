package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.EthSwapRecordFragment
import com.stratagile.qlink.ui.activity.defi.module.EthSwapRecordModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for EthSwapRecordFragment
 * @date 2020/09/02 13:58:17
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EthSwapRecordModule::class))
interface EthSwapRecordComponent {
    fun inject(EthSwapRecordFragment: EthSwapRecordFragment): EthSwapRecordFragment
}