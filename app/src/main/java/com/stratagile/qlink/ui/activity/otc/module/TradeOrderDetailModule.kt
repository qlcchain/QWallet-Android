package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.TradeOrderDetailActivity
import com.stratagile.qlink.ui.activity.otc.contract.TradeOrderDetailContract
import com.stratagile.qlink.ui.activity.otc.presenter.TradeOrderDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of TradeOrderDetailActivity, provide field for TradeOrderDetailActivity
 * @date 2019/07/17 11:14:13
 */
@Module
class TradeOrderDetailModule (private val mView: TradeOrderDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideTradeOrderDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :TradeOrderDetailPresenter {
        return TradeOrderDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTradeOrderDetailActivity() : TradeOrderDetailActivity {
        return mView as TradeOrderDetailActivity
    }
}