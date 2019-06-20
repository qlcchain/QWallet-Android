package com.stratagile.qlink.ui.activity.qlc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.qlc.ImportQlcMnemonicFragment
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcMnemonicModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The component for ImportQlcMnemonicFragment
 * @date 2019/06/06 10:37:31
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(ImportQlcMnemonicModule::class))
interface ImportQlcMnemonicComponent {
    fun inject(ImportQlcMnemonicFragment: ImportQlcMnemonicFragment): ImportQlcMnemonicFragment
}