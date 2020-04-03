package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.VoteActivity
import com.stratagile.qlink.ui.activity.my.contract.VoteContract
import com.stratagile.qlink.ui.activity.my.presenter.VotePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of VoteActivity, provide field for VoteActivity
 * @date 2020/02/26 10:34:02
 */
@Module
class VoteModule (private val mView: VoteContract.View) {

    @Provides
    @ActivityScope
    fun provideVotePresenter(httpAPIWrapper: HttpAPIWrapper) :VotePresenter {
        return VotePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideVoteActivity() : VoteActivity {
        return mView as VoteActivity
    }
}