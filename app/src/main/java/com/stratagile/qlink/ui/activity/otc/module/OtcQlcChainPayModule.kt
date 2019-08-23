package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcQlcChainPayActivity
import com.stratagile.qlink.ui.activity.otc.contract.OtcQlcChainPayContract
import com.stratagile.qlink.ui.activity.otc.presenter.OtcQlcChainPayPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OtcQlcChainPayActivity, provide field for OtcQlcChainPayActivity
 * @date 2019/08/23 12:01:58
 */
@Module
class OtcQlcChainPayModule (private val mView: OtcQlcChainPayContract.View) {

    @Provides
    @ActivityScope
    fun provideOtcQlcChainPayPresenter(httpAPIWrapper: HttpAPIWrapper) :OtcQlcChainPayPresenter {
        return OtcQlcChainPayPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOtcQlcChainPayActivity() : OtcQlcChainPayActivity {
        return mView as OtcQlcChainPayActivity
    }
}