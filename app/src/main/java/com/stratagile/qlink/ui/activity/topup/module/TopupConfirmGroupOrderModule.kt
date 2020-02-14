package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupConfirmGroupOrderActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupConfirmGroupOrderContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupConfirmGroupOrderPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupConfirmGroupOrderActivity, provide field for TopupConfirmGroupOrderActivity
 * @date 2020/02/13 16:15:05
 */
@Module
class TopupConfirmGroupOrderModule (private val mView: TopupConfirmGroupOrderContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupConfirmGroupOrderPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupConfirmGroupOrderPresenter {
        return TopupConfirmGroupOrderPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupConfirmGroupOrderActivity() : TopupConfirmGroupOrderActivity {
        return mView as TopupConfirmGroupOrderActivity
    }
}