package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.ConfirmActivity
import com.stratagile.qlink.ui.activity.defi.contract.ConfirmContract
import com.stratagile.qlink.ui.activity.defi.presenter.ConfirmPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of ConfirmActivity, provide field for ConfirmActivity
 * @date 2020/10/14 16:59:28
 */
@Module
class ConfirmModule (private val mView: ConfirmContract.View) {

    @Provides
    @ActivityScope
    fun provideConfirmPresenter(httpAPIWrapper: HttpAPIWrapper) :ConfirmPresenter {
        return ConfirmPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideConfirmActivity() : ConfirmActivity {
        return mView as ConfirmActivity
    }
}