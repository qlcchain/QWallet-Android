package com.stratagile.qlink.ui.activity.defi.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.defi.WalletConnectActivity
import com.stratagile.qlink.ui.activity.defi.contract.WalletConnectContract
import com.stratagile.qlink.ui.activity.defi.presenter.WalletConnectPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.defi
 * @Description: The moduele of WalletConnectActivity, provide field for WalletConnectActivity
 * @date 2020/09/15 16:03:09
 */
@Module
class WalletConnectModule (private val mView: WalletConnectContract.View) {

    @Provides
    @ActivityScope
    fun provideWalletConnectPresenter(httpAPIWrapper: HttpAPIWrapper) :WalletConnectPresenter {
        return WalletConnectPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideWalletConnectActivity() : WalletConnectActivity {
        return mView as WalletConnectActivity
    }
}