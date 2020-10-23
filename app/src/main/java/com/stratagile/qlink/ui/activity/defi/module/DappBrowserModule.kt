package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DappBrowserActivity
import com.stratagile.qlink.ui.activity.defi.contract.DappBrowserContract
import com.stratagile.qlink.ui.activity.defi.presenter.DappBrowserPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DappBrowserActivity, provide field for DappBrowserActivity
 * @date 2020/10/15 17:56:57
 */
@Module
class DappBrowserModule (private val mView: DappBrowserContract.View) {

    @Provides
    @ActivityScope
    fun provideDappBrowserPresenter(httpAPIWrapper: HttpAPIWrapper) :DappBrowserPresenter {
        return DappBrowserPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDappBrowserActivity() : DappBrowserActivity {
        return mView as DappBrowserActivity
    }
}