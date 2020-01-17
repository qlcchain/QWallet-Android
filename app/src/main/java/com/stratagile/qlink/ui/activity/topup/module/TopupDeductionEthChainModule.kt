package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupDeductionEthChainActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupDeductionEthChainContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupDeductionEthChainPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupDeductionEthChainActivity, provide field for TopupDeductionEthChainActivity
 * @date 2019/12/27 11:59:29
 */
@Module
class TopupDeductionEthChainModule (private val mView: TopupDeductionEthChainContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupDeductionEthChainPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupDeductionEthChainPresenter {
        return TopupDeductionEthChainPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupDeductionEthChainActivity() : TopupDeductionEthChainActivity {
        return mView as TopupDeductionEthChainActivity
    }
}