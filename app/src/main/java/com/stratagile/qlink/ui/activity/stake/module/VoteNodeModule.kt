package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.VoteNodeFragment
import com.stratagile.qlink.ui.activity.stake.contract.VoteNodeContract
import com.stratagile.qlink.ui.activity.stake.presenter.VoteNodePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of VoteNodeFragment, provide field for VoteNodeFragment
 * @date 2019/08/08 16:37:41
 */
@Module
class VoteNodeModule (private val mView: VoteNodeContract.View) {

    @Provides
    @ActivityScope
    fun provideVoteNodePresenter(httpAPIWrapper: HttpAPIWrapper) :VoteNodePresenter {
        return VoteNodePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideVoteNodeFragment() : VoteNodeFragment {
        return mView as VoteNodeFragment
    }
}