package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupSelectDeductionTokenActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupSelectDeductionTokenContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupSelectDeductionTokenPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupSelectDeductionTokenActivity, provide field for TopupSelectDeductionTokenActivity
 * @date 2020/02/13 20:41:09
 */
@Module
class TopupSelectDeductionTokenModule (private val mView: TopupSelectDeductionTokenContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupSelectDeductionTokenPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupSelectDeductionTokenPresenter {
        return TopupSelectDeductionTokenPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupSelectDeductionTokenActivity() : TopupSelectDeductionTokenActivity {
        return mView as TopupSelectDeductionTokenActivity
    }
}