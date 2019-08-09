package com.stratagile.qlink.ui.activity.stake.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity
import com.stratagile.qlink.ui.activity.stake.contract.MyStakeContract
import com.stratagile.qlink.ui.activity.stake.presenter.MyStakePresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.stake
 * @Description: The moduele of MyStakeActivity, provide field for MyStakeActivity
 * @date 2019/08/08 15:32:14
 */
@Module
class MyStakeModule (private val mView: MyStakeContract.View) {

    @Provides
    @ActivityScope
    fun provideMyStakePresenter(httpAPIWrapper: HttpAPIWrapper) :MyStakePresenter {
        return MyStakePresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideMyStakeActivity() : MyStakeActivity {
        return mView as MyStakeActivity
    }
}