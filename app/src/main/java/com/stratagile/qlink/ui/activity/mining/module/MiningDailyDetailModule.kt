package com.stratagile.qlink.ui.activity.mining.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.mining.MiningDailyDetailActivity
import com.stratagile.qlink.ui.activity.mining.contract.MiningDailyDetailContract
import com.stratagile.qlink.ui.activity.mining.presenter.MiningDailyDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: The moduele of MiningDailyDetailActivity, provide field for MiningDailyDetailActivity
 * @date 2019/11/14 18:13:10
 */
@Module
class MiningDailyDetailModule (private val mView: MiningDailyDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideMiningDailyDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :MiningDailyDetailPresenter {
        return MiningDailyDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideMiningDailyDetailActivity() : MiningDailyDetailActivity {
        return mView as MiningDailyDetailActivity
    }
}