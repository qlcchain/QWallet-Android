package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.QurryMobileActivity
import com.stratagile.qlink.ui.activity.topup.module.QurryMobileModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for QurryMobileActivity
 * @date 2019/09/24 14:50:33
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(QurryMobileModule::class))
interface QurryMobileComponent {
    fun inject(QurryMobileActivity: QurryMobileActivity): QurryMobileActivity
}