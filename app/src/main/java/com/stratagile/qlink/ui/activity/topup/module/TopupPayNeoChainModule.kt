package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupPayNeoChainActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupPayNeoChainContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupPayNeoChainPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupPayNeoChainActivity, provide field for TopupPayNeoChainActivity
 * @date 2019/12/26 16:34:35
 */
@Module
class TopupPayNeoChainModule (private val mView: TopupPayNeoChainContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupPayNeoChainPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupPayNeoChainPresenter {
        return TopupPayNeoChainPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupPayNeoChainActivity() : TopupPayNeoChainActivity {
        return mView as TopupPayNeoChainActivity
    }
}