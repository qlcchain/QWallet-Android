package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicClaimQlcActivity
import com.stratagile.qlink.ui.activity.my.contract.EpidemicClaimQlcContract
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicClaimQlcPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of EpidemicClaimQlcActivity, provide field for EpidemicClaimQlcActivity
 * @date 2020/04/16 15:57:15
 */
@Module
class EpidemicClaimQlcModule (private val mView: EpidemicClaimQlcContract.View) {

    @Provides
    @ActivityScope
    fun provideEpidemicClaimQlcPresenter(httpAPIWrapper: HttpAPIWrapper) :EpidemicClaimQlcPresenter {
        return EpidemicClaimQlcPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEpidemicClaimQlcActivity() : EpidemicClaimQlcActivity {
        return mView as EpidemicClaimQlcActivity
    }
}