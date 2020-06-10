package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiKeyStateFragment
import com.stratagile.qlink.ui.activity.defi.contract.DefiKeyStateContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiKeyStatePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiKeyStateFragment, provide field for DefiKeyStateFragment
 * @date 2020/06/02 10:25:39
 */
@Module
class DefiKeyStateModule (private val mView: DefiKeyStateContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiKeyStatePresenter(httpAPIWrapper: HttpAPIWrapper) :DefiKeyStatePresenter {
        return DefiKeyStatePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiKeyStateFragment() : DefiKeyStateFragment {
        return mView as DefiKeyStateFragment
    }
}