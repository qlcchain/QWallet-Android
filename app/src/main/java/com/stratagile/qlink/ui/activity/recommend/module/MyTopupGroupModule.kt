package com.stratagile.qlink.ui.activity.recommend.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.recommend.MyTopupGroupActivity
import com.stratagile.qlink.ui.activity.recommend.contract.MyTopupGroupContract
import com.stratagile.qlink.ui.activity.recommend.presenter.MyTopupGroupPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.recommend
 * @Description: The moduele of MyTopupGroupActivity, provide field for MyTopupGroupActivity
 * @date 2020/01/15 16:21:51
 */
@Module
class MyTopupGroupModule (private val mView: MyTopupGroupContract.View) {

    @Provides
    @ActivityScope
    fun provideMyTopupGroupPresenter(httpAPIWrapper: HttpAPIWrapper) :MyTopupGroupPresenter {
        return MyTopupGroupPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideMyTopupGroupActivity() : MyTopupGroupActivity {
        return mView as MyTopupGroupActivity
    }
}