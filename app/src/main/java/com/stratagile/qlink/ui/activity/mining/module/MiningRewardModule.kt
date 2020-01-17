package com.stratagile.qlink.ui.activity.mining.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.mining.MiningRewardActivity
import com.stratagile.qlink.ui.activity.mining.contract.MiningRewardContract
import com.stratagile.qlink.ui.activity.mining.presenter.MiningRewardPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: The moduele of MiningRewardActivity, provide field for MiningRewardActivity
 * @date 2019/11/15 15:49:47
 */
@Module
class MiningRewardModule (private val mView: MiningRewardContract.View) {

    @Provides
    @ActivityScope
    fun provideMiningRewardPresenter(httpAPIWrapper: HttpAPIWrapper) :MiningRewardPresenter {
        return MiningRewardPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideMiningRewardActivity() : MiningRewardActivity {
        return mView as MiningRewardActivity
    }
}