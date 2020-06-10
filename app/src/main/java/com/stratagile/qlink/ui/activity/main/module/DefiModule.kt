package com.stratagile.qlink.ui.activity.main.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.main.DefiFragment
import com.stratagile.qlink.ui.activity.main.contract.DefiContract
import com.stratagile.qlink.ui.activity.main.presenter.DefiPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of DefiFragment, provide field for DefiFragment
 * @date 2020/05/25 11:29:00
 */
@Module
class DefiModule (private val mView: DefiContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiPresenter(httpAPIWrapper: HttpAPIWrapper) :DefiPresenter {
        return DefiPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiFragment() : DefiFragment {
        return mView as DefiFragment
    }
}