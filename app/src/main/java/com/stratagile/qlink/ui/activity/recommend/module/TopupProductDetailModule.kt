package com.stratagile.qlink.ui.activity.recommend.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.TopupProductDetailActivity
import com.stratagile.qlink.ui.activity.recommend.contract.TopupProductDetailContract
import com.stratagile.qlink.ui.activity.recommend.presenter.TopupProductDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The moduele of TopupProductDetailActivity, provide field for TopupProductDetailActivity
 * @date 2020/01/13 15:36:22
 */
@Module
class TopupProductDetailModule (private val mView: TopupProductDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupProductDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupProductDetailPresenter {
        return TopupProductDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupProductDetailActivity() : TopupProductDetailActivity {
        return mView as TopupProductDetailActivity
    }
}