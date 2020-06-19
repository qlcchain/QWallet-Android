package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiDetailActivity
import com.stratagile.qlink.ui.activity.defi.contract.DefiDetailContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiDetailActivity, provide field for DefiDetailActivity
 * @date 2020/05/29 09:13:19
 */
@Module
class DefiDetailModule (private val mView: DefiDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :DefiDetailPresenter {
        return DefiDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiDetailActivity() : DefiDetailActivity {
        return mView as DefiDetailActivity
    }
}