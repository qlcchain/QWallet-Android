package com.stratagile.qlink.ui.activity.mining.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.mining.MiningInviteActivity
import com.stratagile.qlink.ui.activity.mining.contract.MiningInviteContract
import com.stratagile.qlink.ui.activity.mining.presenter.MiningInvitePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.mining
 * @Description: The moduele of MiningInviteActivity, provide field for MiningInviteActivity
 * @date 2019/11/14 09:43:06
 */
@Module
class MiningInviteModule (private val mView: MiningInviteContract.View) {

    @Provides
    @ActivityScope
    fun provideMiningInvitePresenter(httpAPIWrapper: HttpAPIWrapper) :MiningInvitePresenter {
        return MiningInvitePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideMiningInviteActivity() : MiningInviteActivity {
        return mView as MiningInviteActivity
    }
}