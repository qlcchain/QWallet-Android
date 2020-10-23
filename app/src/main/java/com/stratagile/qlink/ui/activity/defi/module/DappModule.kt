package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DappFragment
import com.stratagile.qlink.ui.activity.defi.contract.DappContract
import com.stratagile.qlink.ui.activity.defi.presenter.DappPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DappFragment, provide field for DappFragment
 * @date 2020/09/17 10:44:40
 */
@Module
class DappModule (private val mView: DappContract.View) {

    @Provides
    @ActivityScope
    fun provideDappPresenter(httpAPIWrapper: HttpAPIWrapper) :DappPresenter {
        return DappPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDappFragment() : DappFragment {
        return mView as DappFragment
    }
}