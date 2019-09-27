package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.TopupQlcPayActivity
import com.stratagile.qlink.ui.activity.topup.contract.TopupQlcPayContract
import com.stratagile.qlink.ui.activity.topup.presenter.TopupQlcPayPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of TopupQlcPayActivity, provide field for TopupQlcPayActivity
 * @date 2019/09/26 10:08:40
 */
@Module
class TopupQlcPayModule (private val mView: TopupQlcPayContract.View) {

    @Provides
    @ActivityScope
    fun provideTopupQlcPayPresenter(httpAPIWrapper: HttpAPIWrapper) :TopupQlcPayPresenter {
        return TopupQlcPayPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideTopupQlcPayActivity() : TopupQlcPayActivity {
        return mView as TopupQlcPayActivity
    }
}