package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.EthTransformActivity
import com.stratagile.qlink.ui.activity.defi.contract.EthTransformContract
import com.stratagile.qlink.ui.activity.defi.presenter.EthTransformPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of EthTransformActivity, provide field for EthTransformActivity
 * @date 2020/08/15 16:13:43
 */
@Module
class EthTransformModule (private val mView: EthTransformContract.View) {

    @Provides
    @ActivityScope
    fun provideEthTransformPresenter(httpAPIWrapper: HttpAPIWrapper) :EthTransformPresenter {
        return EthTransformPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEthTransformActivity() : EthTransformActivity {
        return mView as EthTransformActivity
    }
}