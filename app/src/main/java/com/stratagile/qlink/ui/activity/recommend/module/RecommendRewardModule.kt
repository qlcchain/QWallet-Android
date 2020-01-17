package com.stratagile.qlink.ui.activity.recommend.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.RecommendRewardActivity
import com.stratagile.qlink.ui.activity.recommend.contract.RecommendRewardContract
import com.stratagile.qlink.ui.activity.recommend.presenter.RecommendRewardPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The moduele of RecommendRewardActivity, provide field for RecommendRewardActivity
 * @date 2020/01/09 13:57:40
 */
@Module
class RecommendRewardModule (private val mView: RecommendRewardContract.View) {

    @Provides
    @ActivityScope
    fun provideRecommendRewardPresenter(httpAPIWrapper: HttpAPIWrapper) :RecommendRewardPresenter {
        return RecommendRewardPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideRecommendRewardActivity() : RecommendRewardActivity {
        return mView as RecommendRewardActivity
    }
}