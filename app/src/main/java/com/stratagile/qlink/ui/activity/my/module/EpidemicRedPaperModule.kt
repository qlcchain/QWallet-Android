package com.stratagile.qlink.ui.activity.my.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.my.EpidemicRedPaperActivity
import com.stratagile.qlink.ui.activity.my.contract.EpidemicRedPaperContract
import com.stratagile.qlink.ui.activity.my.presenter.EpidemicRedPaperPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of EpidemicRedPaperActivity, provide field for EpidemicRedPaperActivity
 * @date 2020/04/13 17:05:33
 */
@Module
class EpidemicRedPaperModule (private val mView: EpidemicRedPaperContract.View) {

    @Provides
    @ActivityScope
    fun provideEpidemicRedPaperPresenter(httpAPIWrapper: HttpAPIWrapper) :EpidemicRedPaperPresenter {
        return EpidemicRedPaperPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEpidemicRedPaperActivity() : EpidemicRedPaperActivity {
        return mView as EpidemicRedPaperActivity
    }
}