package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiListFragment
import com.stratagile.qlink.ui.activity.defi.contract.DefiListContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiListPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiListFragment, provide field for DefiListFragment
 * @date 2020/05/25 17:10:05
 */
@Module
class DefiListModule (private val mView: DefiListContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiListPresenter(httpAPIWrapper: HttpAPIWrapper) :DefiListPresenter {
        return DefiListPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiListFragment() : DefiListFragment {
        return mView as DefiListFragment
    }
}