package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.AppealsFragment
import com.stratagile.qlink.ui.activity.otc.contract.AppealsContract
import com.stratagile.qlink.ui.activity.otc.presenter.AppealsPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of AppealsFragment, provide field for AppealsFragment
 * @date 2019/07/19 11:45:27
 */
@Module
class AppealsModule (private val mView: AppealsContract.View) {

    @Provides
    @ActivityScope
    fun provideAppealsPresenter(httpAPIWrapper: HttpAPIWrapper) :AppealsPresenter {
        return AppealsPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideAppealsFragment() : AppealsFragment {
        return mView as AppealsFragment
    }
}