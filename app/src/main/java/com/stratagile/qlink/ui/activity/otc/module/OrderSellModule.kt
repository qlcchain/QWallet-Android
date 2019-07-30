package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderSellFragment
import com.stratagile.qlink.ui.activity.otc.contract.OrderSellContract
import com.stratagile.qlink.ui.activity.otc.presenter.OrderSellPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OrderSellFragment, provide field for OrderSellFragment
 * @date 2019/07/08 17:24:46
 */
@Module
class OrderSellModule (private val mView: OrderSellContract.View) {

    @Provides
    @ActivityScope
    fun provideOrderSellPresenter(httpAPIWrapper: HttpAPIWrapper) :OrderSellPresenter {
        return OrderSellPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOrderSellFragment() : OrderSellFragment {
        return mView as OrderSellFragment
    }
}