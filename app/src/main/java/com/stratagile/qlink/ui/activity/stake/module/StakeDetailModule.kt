package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.StakeDetailActivity
import com.stratagile.qlink.ui.activity.stake.contract.StakeDetailContract
import com.stratagile.qlink.ui.activity.stake.presenter.StakeDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of StakeDetailActivity, provide field for StakeDetailActivity
 * @date 2019/08/09 15:26:02
 */
@Module
class StakeDetailModule (private val mView: StakeDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideStakeDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :StakeDetailPresenter {
        return StakeDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideStakeDetailActivity() : StakeDetailActivity {
        return mView as StakeDetailActivity
    }
}