package com.stratagile.qlink.ui.activity.defi.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.TransformQlcActivity
import com.stratagile.qlink.ui.activity.defi.module.TransformQlcModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The component for TransformQlcActivity
 * @date 2020/08/12 15:15:34
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(TransformQlcModule::class))
interface TransformQlcComponent {
    fun inject(TransformQlcActivity: TransformQlcActivity): TransformQlcActivity
}