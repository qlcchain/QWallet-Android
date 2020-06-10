package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiNewsDetailActivity
import com.stratagile.qlink.ui.activity.defi.contract.DefiNewsDetailContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiNewsDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiNewsDetailActivity, provide field for DefiNewsDetailActivity
 * @date 2020/06/05 14:09:12
 */
@Module
class DefiNewsDetailModule (private val mView: DefiNewsDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiNewsDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :DefiNewsDetailPresenter {
        return DefiNewsDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiNewsDetailActivity() : DefiNewsDetailActivity {
        return mView as DefiNewsDetailActivity
    }
}