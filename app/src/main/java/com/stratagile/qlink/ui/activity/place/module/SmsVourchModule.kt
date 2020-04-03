package com.stratagile.qlink.ui.activity.place.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.place.SmsVourchActivity
import com.stratagile.qlink.ui.activity.place.contract.SmsVourchContract
import com.stratagile.qlink.ui.activity.place.presenter.SmsVourchPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.place
 * @Description: The moduele of SmsVourchActivity, provide field for SmsVourchActivity
 * @date 2020/02/20 22:53:22
 */
@Module
class SmsVourchModule (private val mView: SmsVourchContract.View) {

    @Provides
    @ActivityScope
    fun provideSmsVourchPresenter(httpAPIWrapper: HttpAPIWrapper) :SmsVourchPresenter {
        return SmsVourchPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSmsVourchActivity() : SmsVourchActivity {
        return mView as SmsVourchActivity
    }
}