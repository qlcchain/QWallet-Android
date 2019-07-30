package com.stratagile.qlink.ui.activity.neo.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.neo.NeoTestActivity
import com.stratagile.qlink.ui.activity.neo.contract.NeoTestContract
import com.stratagile.qlink.ui.activity.neo.presenter.NeoTestPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.neo
 * @Description: The moduele of NeoTestActivity, provide field for NeoTestActivity
 * @date 2019/06/10 10:30:03
 */
@Module
class NeoTestModule (private val mView: NeoTestContract.View) {

    @Provides
    @ActivityScope
    fun provideNeoTestPresenter(httpAPIWrapper: HttpAPIWrapper) :NeoTestPresenter {
        return NeoTestPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideNeoTestActivity() : NeoTestActivity {
        return mView as NeoTestActivity
    }
}