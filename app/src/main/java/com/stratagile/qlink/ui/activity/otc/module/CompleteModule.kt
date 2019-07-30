package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.CompleteFragment
import com.stratagile.qlink.ui.activity.otc.contract.CompleteContract
import com.stratagile.qlink.ui.activity.otc.presenter.CompletePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of CompleteFragment, provide field for CompleteFragment
 * @date 2019/07/16 17:53:03
 */
@Module
class CompleteModule (private val mView: CompleteContract.View) {

    @Provides
    @ActivityScope
    fun provideCompletePresenter(httpAPIWrapper: HttpAPIWrapper) :CompletePresenter {
        return CompletePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideCompleteFragment() : CompleteFragment {
        return mView as CompleteFragment
    }
}