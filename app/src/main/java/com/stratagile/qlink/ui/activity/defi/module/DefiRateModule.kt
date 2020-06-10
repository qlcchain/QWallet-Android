package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiRateActivity
import com.stratagile.qlink.ui.activity.defi.contract.DefiRateContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiRatePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiRateActivity, provide field for DefiRateActivity
 * @date 2020/06/03 14:17:18
 */
@Module
class DefiRateModule (private val mView: DefiRateContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiRatePresenter(httpAPIWrapper: HttpAPIWrapper) :DefiRatePresenter {
        return DefiRatePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiRateActivity() : DefiRateActivity {
        return mView as DefiRateActivity
    }
}