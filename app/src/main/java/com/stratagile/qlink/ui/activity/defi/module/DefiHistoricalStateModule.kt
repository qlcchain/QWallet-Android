package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiHistoricalStateFragment
import com.stratagile.qlink.ui.activity.defi.contract.DefiHistoricalStateContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiHistoricalStatePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiHistoricalStateFragment, provide field for DefiHistoricalStateFragment
 * @date 2020/06/02 10:26:24
 */
@Module
class DefiHistoricalStateModule (private val mView: DefiHistoricalStateContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiHistoricalStatePresenter(httpAPIWrapper: HttpAPIWrapper) :DefiHistoricalStatePresenter {
        return DefiHistoricalStatePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiHistoricalStateFragment() : DefiHistoricalStateFragment {
        return mView as DefiHistoricalStateFragment
    }
}