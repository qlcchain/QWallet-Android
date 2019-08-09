package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.ConfidantFragment
import com.stratagile.qlink.ui.activity.stake.contract.ConfidantContract
import com.stratagile.qlink.ui.activity.stake.presenter.ConfidantPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of ConfidantFragment, provide field for ConfidantFragment
 * @date 2019/08/08 16:38:01
 */
@Module
class ConfidantModule (private val mView: ConfidantContract.View) {

    @Provides
    @ActivityScope
    fun provideConfidantPresenter(httpAPIWrapper: HttpAPIWrapper) :ConfidantPresenter {
        return ConfidantPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideConfidantFragment() : ConfidantFragment {
        return mView as ConfidantFragment
    }
}