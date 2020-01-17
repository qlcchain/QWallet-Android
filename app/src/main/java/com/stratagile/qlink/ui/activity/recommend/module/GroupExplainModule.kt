package com.stratagile.qlink.ui.activity.recommend.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.GroupExplainActivity
import com.stratagile.qlink.ui.activity.recommend.contract.GroupExplainContract
import com.stratagile.qlink.ui.activity.recommend.presenter.GroupExplainPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The moduele of GroupExplainActivity, provide field for GroupExplainActivity
 * @date 2020/01/17 13:37:58
 */
@Module
class GroupExplainModule (private val mView: GroupExplainContract.View) {

    @Provides
    @ActivityScope
    fun provideGroupExplainPresenter(httpAPIWrapper: HttpAPIWrapper) :GroupExplainPresenter {
        return GroupExplainPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideGroupExplainActivity() : GroupExplainActivity {
        return mView as GroupExplainActivity
    }
}