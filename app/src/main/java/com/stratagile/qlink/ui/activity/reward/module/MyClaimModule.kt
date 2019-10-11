package com.stratagile.qlink.ui.activity.reward.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.reward.MyClaimActivity
import com.stratagile.qlink.ui.activity.reward.contract.MyClaimContract
import com.stratagile.qlink.ui.activity.reward.presenter.MyClaimPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.claim
 * @Description: The moduele of MyClaimActivity, provide field for MyClaimActivity
 * @date 2019/10/09 11:57:31
 */
@Module
class MyClaimModule (private val mView: MyClaimContract.View) {

    @Provides
    @ActivityScope
    fun provideMyClaimPresenter(httpAPIWrapper: HttpAPIWrapper) :MyClaimPresenter {
        return MyClaimPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideMyClaimActivity() : MyClaimActivity {
        return mView as MyClaimActivity
    }
}