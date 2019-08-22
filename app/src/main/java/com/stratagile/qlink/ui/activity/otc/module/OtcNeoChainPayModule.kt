package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcNeoChainPayActivity
import com.stratagile.qlink.ui.activity.otc.contract.OtcNeoChainPayContract
import com.stratagile.qlink.ui.activity.otc.presenter.OtcNeoChainPayPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OtcNeoChainPayActivity, provide field for OtcNeoChainPayActivity
 * @date 2019/08/21 15:07:05
 */
@Module
class OtcNeoChainPayModule (private val mView: OtcNeoChainPayContract.View) {

    @Provides
    @ActivityScope
    fun provideOtcNeoChainPayPresenter(httpAPIWrapper: HttpAPIWrapper) :OtcNeoChainPayPresenter {
        return OtcNeoChainPayPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOtcNeoChainPayActivity() : OtcNeoChainPayActivity {
        return mView as OtcNeoChainPayActivity
    }
}