package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealSubmittedActivity
import com.stratagile.qlink.ui.activity.otc.contract.AppealSubmittedContract
import com.stratagile.qlink.ui.activity.otc.presenter.AppealSubmittedPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of AppealSubmittedActivity, provide field for AppealSubmittedActivity
 * @date 2019/07/22 15:34:25
 */
@Module
class AppealSubmittedModule (private val mView: AppealSubmittedContract.View) {

    @Provides
    @ActivityScope
    fun provideAppealSubmittedPresenter(httpAPIWrapper: HttpAPIWrapper) :AppealSubmittedPresenter {
        return AppealSubmittedPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideAppealSubmittedActivity() : AppealSubmittedActivity {
        return mView as AppealSubmittedActivity
    }
}