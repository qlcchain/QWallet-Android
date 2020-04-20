package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicClaimActivity
import com.stratagile.qlink.ui.activity.my.contract.EpidemicClaimContract
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicClaimPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of EpidemicClaimActivity, provide field for EpidemicClaimActivity
 * @date 2020/04/15 17:22:27
 */
@Module
class EpidemicClaimModule (private val mView: EpidemicClaimContract.View) {

    @Provides
    @ActivityScope
    fun provideEpidemicClaimPresenter(httpAPIWrapper: HttpAPIWrapper) :EpidemicClaimPresenter {
        return EpidemicClaimPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEpidemicClaimActivity() : EpidemicClaimActivity {
        return mView as EpidemicClaimActivity
    }
}