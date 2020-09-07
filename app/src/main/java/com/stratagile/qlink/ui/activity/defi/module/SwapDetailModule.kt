package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.SwapDetailActivity
import com.stratagile.qlink.ui.activity.defi.contract.SwapDetailContract
import com.stratagile.qlink.ui.activity.defi.presenter.SwapDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of SwapDetailActivity, provide field for SwapDetailActivity
 * @date 2020/08/25 11:39:23
 */
@Module
class SwapDetailModule (private val mView: SwapDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideSwapDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :SwapDetailPresenter {
        return SwapDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSwapDetailActivity() : SwapDetailActivity {
        return mView as SwapDetailActivity
    }
}