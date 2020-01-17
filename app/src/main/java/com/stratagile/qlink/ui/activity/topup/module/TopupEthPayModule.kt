package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupEthPayActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupEthPayContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupEthPayPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupEthPayActivity, provide field for TopupEthPayActivity
 * @date 2019/10/24 10:18:43
 */
@Module
class TopupEthPayModule (private val mView: TopupEthPayContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupEthPayPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupEthPayPresenter {
        return TopupEthPayPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupEthPayActivity() : TopupEthPayActivity {
        return mView as TopupEthPayActivity
    }
}