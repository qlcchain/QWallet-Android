package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.DefiActiveDataFragment
import com.stratagile.qlink.ui.activity.defi.contract.DefiActiveDataContract
import com.stratagile.qlink.ui.activity.defi.presenter.DefiActiveDataPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of DefiActiveDataFragment, provide field for DefiActiveDataFragment
 * @date 2020/06/02 10:25:59
 */
@Module
class DefiActiveDataModule (private val mView: DefiActiveDataContract.View) {

    @Provides
    @ActivityScope
    fun provideDefiActiveDataPresenter(httpAPIWrapper: HttpAPIWrapper) :DefiActiveDataPresenter {
        return DefiActiveDataPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideDefiActiveDataFragment() : DefiActiveDataFragment {
        return mView as DefiActiveDataFragment
    }
}