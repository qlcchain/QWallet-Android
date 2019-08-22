package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.TradeListFragment
import com.stratagile.qlink.ui.activity.otc.contract.TradeListContract
import com.stratagile.qlink.ui.activity.otc.presenter.TradeListPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of TradeListFragment, provide field for TradeListFragment
 * @date 2019/08/19 09:38:46
 */
@Module
class TradeListModule (private val mView: TradeListContract.View) {

    @Provides
    @ActivityScope
    fun provideTradeListPresenter(httpAPIWrapper: HttpAPIWrapper) :TradeListPresenter {
        return TradeListPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTradeListFragment() : TradeListFragment {
        return mView as TradeListFragment
    }
}