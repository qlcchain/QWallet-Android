package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.EthSwapRecordFragment
import com.stratagile.qlink.ui.activity.defi.contract.EthSwapRecordContract
import com.stratagile.qlink.ui.activity.defi.presenter.EthSwapRecordPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of EthSwapRecordFragment, provide field for EthSwapRecordFragment
 * @date 2020/09/02 13:58:17
 */
@Module
class EthSwapRecordModule (private val mView: EthSwapRecordContract.View) {

    @Provides
    @ActivityScope
    fun provideEthSwapRecordPresenter(httpAPIWrapper: HttpAPIWrapper) :EthSwapRecordPresenter {
        return EthSwapRecordPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideEthSwapRecordFragment() : EthSwapRecordFragment {
        return mView as EthSwapRecordFragment
    }
}