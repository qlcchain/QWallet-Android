package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.ProcessFragment
import com.stratagile.qlink.ui.activity.otc.contract.ProcessContract
import com.stratagile.qlink.ui.activity.otc.presenter.ProcessPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of ProcessFragment, provide field for ProcessFragment
 * @date 2019/07/16 17:52:45
 */
@Module
class ProcessModule (private val mView: ProcessContract.View) {

    @Provides
    @ActivityScope
    fun provideProcessPresenter(httpAPIWrapper: HttpAPIWrapper) :ProcessPresenter {
        return ProcessPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideProcessFragment() : ProcessFragment {
        return mView as ProcessFragment
    }
}