package com.stratagile.qlink.ui.activity.reward.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.reward.ClaimRewardActivity
import com.stratagile.qlink.ui.activity.reward.contract.ClaimRewardContract
import com.stratagile.qlink.ui.activity.reward.presenter.ClaimRewardPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.reward
 * @Description: The moduele of ClaimRewardActivity, provide field for ClaimRewardActivity
 * @date 2019/10/10 15:28:24
 */
@Module
class ClaimRewardModule (private val mView: ClaimRewardContract.View) {

    @Provides
    @ActivityScope
    fun provideClaimRewardPresenter(httpAPIWrapper: HttpAPIWrapper) :ClaimRewardPresenter {
        return ClaimRewardPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideClaimRewardActivity() : ClaimRewardActivity {
        return mView as ClaimRewardActivity
    }
}