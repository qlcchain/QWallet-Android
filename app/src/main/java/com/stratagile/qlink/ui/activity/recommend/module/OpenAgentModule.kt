package com.stratagile.qlink.ui.activity.recommend.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.OpenAgentActivity
import com.stratagile.qlink.ui.activity.recommend.contract.OpenAgentContract
import com.stratagile.qlink.ui.activity.recommend.presenter.OpenAgentPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The moduele of OpenAgentActivity, provide field for OpenAgentActivity
 * @date 2020/01/09 13:59:03
 */
@Module
class OpenAgentModule (private val mView: OpenAgentContract.View) {

    @Provides
    @ActivityScope
    fun provideOpenAgentPresenter(httpAPIWrapper: HttpAPIWrapper) :OpenAgentPresenter {
        return OpenAgentPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOpenAgentActivity() : OpenAgentActivity {
        return mView as OpenAgentActivity
    }
}