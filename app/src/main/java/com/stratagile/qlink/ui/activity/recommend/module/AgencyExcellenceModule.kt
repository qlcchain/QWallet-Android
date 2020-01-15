package com.stratagile.qlink.ui.activity.recommend.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.AgencyExcellenceActivity
import com.stratagile.qlink.ui.activity.recommend.contract.AgencyExcellenceContract
import com.stratagile.qlink.ui.activity.recommend.presenter.AgencyExcellencePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The moduele of AgencyExcellenceActivity, provide field for AgencyExcellenceActivity
 * @date 2020/01/09 13:58:26
 */
@Module
class AgencyExcellenceModule (private val mView: AgencyExcellenceContract.View) {

    @Provides
    @ActivityScope
    fun provideAgencyExcellencePresenter(httpAPIWrapper: HttpAPIWrapper) :AgencyExcellencePresenter {
        return AgencyExcellencePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideAgencyExcellenceActivity() : AgencyExcellenceActivity {
        return mView as AgencyExcellenceActivity
    }
}