package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DappHomeFragment
import com.stratagile.qlink.ui.activity.defi.contract.DappHomeContract
import com.stratagile.qlink.ui.activity.defi.presenter.DappHomePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DappHomeFragment, provide field for DappHomeFragment
 * @date 2020/10/15 16:00:43
 */
@Module
class DappHomeModule (private val mView: DappHomeContract.View) {

    @Provides
    @ActivityScope
    fun provideDappHomePresenter(httpAPIWrapper: HttpAPIWrapper) :DappHomePresenter {
        return DappHomePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDappHomeFragment() : DappHomeFragment {
        return mView as DappHomeFragment
    }
}