package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SwapRecordFragment
import com.stratagile.qlink.ui.activity.defi.contract.SwapRecordContract
import com.stratagile.qlink.ui.activity.defi.presenter.SwapRecordPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of SwapRecordFragment, provide field for SwapRecordFragment
 * @date 2020/08/12 15:49:25
 */
@Module
class SwapRecordModule (private val mView: SwapRecordContract.View) {

    @Provides
    @ActivityScope
    fun provideSwapRecordPresenter(httpAPIWrapper: HttpAPIWrapper) :SwapRecordPresenter {
        return SwapRecordPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSwapRecordFragment() : SwapRecordFragment {
        return mView as SwapRecordFragment
    }
}