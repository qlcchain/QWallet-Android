package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupDeductionQlcChainActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupDeductionQlcChainContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupDeductionQlcChainPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupDeductionQlcChainActivity, provide field for TopupDeductionQlcChainActivity
 * @date 2019/12/26 14:45:12
 */
@Module
class TopupDeductionQlcChainModule (private val mView: TopupDeductionQlcChainContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupDeductionQlcChainPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupDeductionQlcChainPresenter {
        return TopupDeductionQlcChainPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupDeductionQlcChainActivity() : TopupDeductionQlcChainActivity {
        return mView as TopupDeductionQlcChainActivity
    }
}