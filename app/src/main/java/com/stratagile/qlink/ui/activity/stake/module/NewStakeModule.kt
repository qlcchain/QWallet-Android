package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.NewStakeActivity
import com.stratagile.qlink.ui.activity.stake.contract.NewStakeContract
import com.stratagile.qlink.ui.activity.stake.presenter.NewStakePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of NewStakeActivity, provide field for NewStakeActivity
 * @date 2019/08/08 16:33:44
 */
@Module
class NewStakeModule (private val mView: NewStakeContract.View) {

    @Provides
    @ActivityScope
    fun provideNewStakePresenter(httpAPIWrapper: HttpAPIWrapper) :NewStakePresenter {
        return NewStakePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideNewStakeActivity() : NewStakeActivity {
        return mView as NewStakeActivity
    }
}