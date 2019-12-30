package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.SelectRegionActivity
import com.stratagile.qlink.ui.activity.topup.contract.SelectRegionContract
import com.stratagile.qlink.ui.activity.topup.presenter.SelectRegionPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of SelectRegionActivity, provide field for SelectRegionActivity
 * @date 2019/12/25 14:50:07
 */
@Module
class SelectRegionModule (private val mView: SelectRegionContract.View) {

    @Provides
    @ActivityScope
    fun provideSelectRegionPresenter(httpAPIWrapper: HttpAPIWrapper) :SelectRegionPresenter {
        return SelectRegionPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSelectRegionActivity() : SelectRegionActivity {
        return mView as SelectRegionActivity
    }
}