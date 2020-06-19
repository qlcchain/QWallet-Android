package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiNewsFragment
import com.stratagile.qlink.ui.activity.defi.contract.DefiNewsContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiNewsPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiNewsFragment, provide field for DefiNewsFragment
 * @date 2020/05/25 17:10:21
 */
@Module
class DefiNewsModule (private val mView: DefiNewsContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiNewsPresenter(httpAPIWrapper: HttpAPIWrapper) :DefiNewsPresenter {
        return DefiNewsPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiNewsFragment() : DefiNewsFragment {
        return mView as DefiNewsFragment
    }
}