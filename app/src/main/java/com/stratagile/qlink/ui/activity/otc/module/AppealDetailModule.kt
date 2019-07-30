package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealDetailActivity
import com.stratagile.qlink.ui.activity.otc.contract.AppealDetailContract
import com.stratagile.qlink.ui.activity.otc.presenter.AppealDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of AppealDetailActivity, provide field for AppealDetailActivity
 * @date 2019/07/22 10:13:43
 */
@Module
class AppealDetailModule (private val mView: AppealDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideAppealDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :AppealDetailPresenter {
        return AppealDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideAppealDetailActivity() : AppealDetailActivity {
        return mView as AppealDetailActivity
    }
}