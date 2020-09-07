package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SwapFragment
import com.stratagile.qlink.ui.activity.defi.contract.SwapContract
import com.stratagile.qlink.ui.activity.defi.presenter.SwapPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of SwapFragment, provide field for SwapFragment
 * @date 2020/08/12 15:49:07
 */
@Module
class SwapModule (private val mView: SwapContract.View) {

    @Provides
    @ActivityScope
    fun provideSwapPresenter(httpAPIWrapper: HttpAPIWrapper) :SwapPresenter {
        return SwapPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSwapFragment() : SwapFragment {
        return mView as SwapFragment
    }
}