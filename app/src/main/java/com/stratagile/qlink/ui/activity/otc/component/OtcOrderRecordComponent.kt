package com.stratagile.qlink.ui.activity.otc.component

import com.stratagile.qlink.application.AppComponent
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcOrderRecordActivity
import com.stratagile.qlink.ui.activity.otc.module.OtcOrderRecordModule

import dagger.Component

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The component for OtcOrderRecordActivity
 * @date 2019/07/16 17:43:20
 */
@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(OtcOrderRecordModule::class))
interface OtcOrderRecordComponent {
    fun inject(OtcOrderRecordActivity: OtcOrderRecordActivity): OtcOrderRecordActivity
}