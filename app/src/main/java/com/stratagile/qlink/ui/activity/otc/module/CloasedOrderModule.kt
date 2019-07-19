package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.ClosedOrderFragment
import com.stratagile.qlink.ui.activity.otc.contract.CloasedOrderContract
import com.stratagile.qlink.ui.activity.otc.presenter.CloasedOrderPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of CloasedOrderFragment, provide field for CloasedOrderFragment
 * @date 2019/07/17 14:39:46
 */
@Module
class CloasedOrderModule (private val mView: CloasedOrderContract.View) {

    @Provides
    @ActivityScope
    fun provideCloasedOrderPresenter(httpAPIWrapper: HttpAPIWrapper) :CloasedOrderPresenter {
        return CloasedOrderPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideCloasedOrderFragment() : ClosedOrderFragment {
        return mView as ClosedOrderFragment
    }
}