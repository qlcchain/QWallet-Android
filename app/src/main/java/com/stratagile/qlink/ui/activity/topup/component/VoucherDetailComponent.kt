package com.stratagile.qlink.ui.activity.topup.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.VoucherDetailActivity
import com.stratagile.qlink.ui.activity.topup.module.VoucherDetailModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The component for VoucherDetailActivity
 * @date 2019/10/17 17:29:37
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(VoucherDetailModule::class))
interface VoucherDetailComponent {
    fun inject(VoucherDetailActivity: VoucherDetailActivity): VoucherDetailActivity
}