package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopUpFragment
import com.stratagile.qlink.ui.activity.topup.contract.TopUpContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopUpPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopUpFragment, provide field for TopUpFragment
 * @date 2019/09/23 15:54:17
 */
@Module
class TopUpModule (private val mView: TopUpContract.View) {

    @Provides
    @ActivityScope
    fun provideTopUpPresenter(httpAPIWrapper: HttpAPIWrapper) :TopUpPresenter {
        return TopUpPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopUpFragment() : TopUpFragment {
        return mView as TopUpFragment
    }
}