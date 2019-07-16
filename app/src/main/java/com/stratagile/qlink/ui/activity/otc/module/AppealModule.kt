package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealFragment
import com.stratagile.qlink.ui.activity.otc.contract.AppealContract
import com.stratagile.qlink.ui.activity.otc.presenter.AppealPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of AppealFragment, provide field for AppealFragment
 * @date 2019/07/16 17:53:24
 */
@Module
class AppealModule (private val mView: AppealContract.View) {

    @Provides
    @ActivityScope
    fun provideAppealPresenter(httpAPIWrapper: HttpAPIWrapper) :AppealPresenter {
        return AppealPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideAppealFragment() : AppealFragment {
        return mView as AppealFragment
    }
}