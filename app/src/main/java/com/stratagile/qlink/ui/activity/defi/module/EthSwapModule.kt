package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.EthSwapFragment
import com.stratagile.qlink.ui.activity.defi.contract.EthSwapContract
import com.stratagile.qlink.ui.activity.defi.presenter.EthSwapPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of EthSwapFragment, provide field for EthSwapFragment
 * @date 2020/08/15 16:40:33
 */
@Module
class EthSwapModule (private val mView: EthSwapContract.View) {

    @Provides
    @ActivityScope
    fun provideEthSwapPresenter(httpAPIWrapper: HttpAPIWrapper) :EthSwapPresenter {
        return EthSwapPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEthSwapFragment() : EthSwapFragment {
        return mView as EthSwapFragment
    }
}