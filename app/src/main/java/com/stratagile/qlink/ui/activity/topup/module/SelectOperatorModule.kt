package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.SelectOperatorActivity
import com.stratagile.qlink.ui.activity.topup.contract.SelectOperatorContract
import com.stratagile.qlink.ui.activity.topup.presenter.SelectOperatorPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of SelectOperatorActivity, provide field for SelectOperatorActivity
 * @date 2019/12/25 10:21:32
 */
@Module
class SelectOperatorModule (private val mView: SelectOperatorContract.View) {

    @Provides
    @ActivityScope
    fun provideSelectOperatorPresenter(httpAPIWrapper: HttpAPIWrapper) :SelectOperatorPresenter {
        return SelectOperatorPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSelectOperatorActivity() : SelectOperatorActivity {
        return mView as SelectOperatorActivity
    }
}