package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcOrderRecordActivity
import com.stratagile.qlink.ui.activity.otc.contract.OtcOrderRecordContract
import com.stratagile.qlink.ui.activity.otc.presenter.OtcOrderRecordPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OtcOrderRecordActivity, provide field for OtcOrderRecordActivity
 * @date 2019/07/16 17:43:20
 */
@Module
class OtcOrderRecordModule (private val mView: OtcOrderRecordContract.View) {

    @Provides
    @ActivityScope
    fun provideOtcOrderRecordPresenter(httpAPIWrapper: HttpAPIWrapper) :OtcOrderRecordPresenter {
        return OtcOrderRecordPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOtcOrderRecordActivity() : OtcOrderRecordActivity {
        return mView as OtcOrderRecordActivity
    }
}