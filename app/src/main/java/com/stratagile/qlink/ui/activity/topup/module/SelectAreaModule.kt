package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.SelectAreaActivity
import com.stratagile.qlink.ui.activity.topup.contract.SelectAreaContract
import com.stratagile.qlink.ui.activity.topup.presenter.SelectAreaPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of SelectAreaActivity, provide field for SelectAreaActivity
 * @date 2019/09/24 16:07:07
 */
@Module
class SelectAreaModule (private val mView: SelectAreaContract.View) {

    @Provides
    @ActivityScope
    fun provideSelectAreaPresenter(httpAPIWrapper: HttpAPIWrapper) :SelectAreaPresenter {
        return SelectAreaPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSelectAreaActivity() : SelectAreaActivity {
        return mView as SelectAreaActivity
    }
}