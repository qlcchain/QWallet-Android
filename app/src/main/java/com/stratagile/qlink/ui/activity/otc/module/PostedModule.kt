package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.PostedFragment
import com.stratagile.qlink.ui.activity.otc.contract.PostedContract
import com.stratagile.qlink.ui.activity.otc.presenter.PostedPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of PostedFragment, provide field for PostedFragment
 * @date 2019/07/16 17:52:28
 */
@Module
class PostedModule (private val mView: PostedContract.View) {

    @Provides
    @ActivityScope
    fun providePostedPresenter(httpAPIWrapper: HttpAPIWrapper) :PostedPresenter {
        return PostedPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun providePostedFragment() : PostedFragment {
        return mView as PostedFragment
    }
}