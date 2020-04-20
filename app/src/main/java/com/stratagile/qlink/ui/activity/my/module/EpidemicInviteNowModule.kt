package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicInviteNowActivity
import com.stratagile.qlink.ui.activity.my.contract.EpidemicInviteNowContract
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicInviteNowPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of EpidemicInviteNowActivity, provide field for EpidemicInviteNowActivity
 * @date 2020/04/17 10:25:30
 */
@Module
class EpidemicInviteNowModule (private val mView: EpidemicInviteNowContract.View) {

    @Provides
    @ActivityScope
    fun provideEpidemicInviteNowPresenter(httpAPIWrapper: HttpAPIWrapper) :EpidemicInviteNowPresenter {
        return EpidemicInviteNowPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEpidemicInviteNowActivity() : EpidemicInviteNowActivity {
        return mView as EpidemicInviteNowActivity
    }
}