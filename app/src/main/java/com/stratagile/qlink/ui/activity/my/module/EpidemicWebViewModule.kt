package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicWebViewActivity
import com.stratagile.qlink.ui.activity.my.contract.EpidemicWebViewContract
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicWebViewPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of EpidemicWebViewActivity, provide field for EpidemicWebViewActivity
 * @date 2020/04/16 17:48:35
 */
@Module
class EpidemicWebViewModule (private val mView: EpidemicWebViewContract.View) {

    @Provides
    @ActivityScope
    fun provideEpidemicWebViewPresenter(httpAPIWrapper: HttpAPIWrapper) :EpidemicWebViewPresenter {
        return EpidemicWebViewPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEpidemicWebViewActivity() : EpidemicWebViewActivity {
        return mView as EpidemicWebViewActivity
    }
}