package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderDetailActivity
import com.stratagile.qlink.ui.activity.otc.contract.OrderDetailContract
import com.stratagile.qlink.ui.activity.otc.presenter.OrderDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OrderDetailActivity, provide field for OrderDetailActivity
 * @date 2019/07/10 10:03:30
 */
@Module
class OrderDetailModule (private val mView: OrderDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideOrderDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :OrderDetailPresenter {
        return OrderDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOrderDetailActivity() : OrderDetailActivity {
        return mView as OrderDetailActivity
    }
}