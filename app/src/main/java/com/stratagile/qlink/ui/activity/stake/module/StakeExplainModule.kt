package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.StakeExplainActivity
import com.stratagile.qlink.ui.activity.stake.contract.StakeExplainContract
import com.stratagile.qlink.ui.activity.stake.presenter.StakeExplainPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of StakeExplainActivity, provide field for StakeExplainActivity
 * @date 2019/08/09 15:25:43
 */
@Module
class StakeExplainModule (private val mView: StakeExplainContract.View) {

    @Provides
    @ActivityScope
    fun provideStakeExplainPresenter(httpAPIWrapper: HttpAPIWrapper) :StakeExplainPresenter {
        return StakeExplainPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideStakeExplainActivity() : StakeExplainActivity {
        return mView as StakeExplainActivity
    }
}