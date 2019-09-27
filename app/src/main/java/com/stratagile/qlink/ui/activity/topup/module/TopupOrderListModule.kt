package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupOrderListActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupOrderListContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupOrderListPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupOrderListActivity, provide field for TopupOrderListActivity
 * @date 2019/09/26 15:00:15
 */
@Module
class TopupOrderListModule (private val mView: TopupOrderListContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupOrderListPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupOrderListPresenter {
        return TopupOrderListPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupOrderListActivity() : TopupOrderListActivity {
        return mView as TopupOrderListActivity
    }
}