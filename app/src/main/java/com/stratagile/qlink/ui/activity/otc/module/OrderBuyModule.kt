package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderBuyFragment
import com.stratagile.qlink.ui.activity.otc.contract.OrderBuyContract
import com.stratagile.qlink.ui.activity.otc.presenter.OrderBuyPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OrderBuyFragment, provide field for OrderBuyFragment
 * @date 2019/07/08 17:24:58
 */
@Module
class OrderBuyModule (private val mView: OrderBuyContract.View) {

    @Provides
    @ActivityScope
    fun provideOrderBuyPresenter(httpAPIWrapper: HttpAPIWrapper) :OrderBuyPresenter {
        return OrderBuyPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOrderBuyFragment() : OrderBuyFragment {
        return mView as OrderBuyFragment
    }
}