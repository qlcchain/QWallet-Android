package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.UsdtPayActivity
import com.stratagile.qlink.ui.activity.otc.contract.UsdtPayContract
import com.stratagile.qlink.ui.activity.otc.presenter.UsdtPayPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of UsdtPayActivity, provide field for UsdtPayActivity
 * @date 2019/07/31 13:58:11
 */
@Module
class UsdtPayModule (private val mView: UsdtPayContract.View) {

    @Provides
    @ActivityScope
    fun provideUsdtPayPresenter(httpAPIWrapper: HttpAPIWrapper) :UsdtPayPresenter {
        return UsdtPayPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideUsdtPayActivity() : UsdtPayActivity {
        return mView as UsdtPayActivity
    }
}