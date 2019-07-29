package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OtcChooseWalletActivity
import com.stratagile.qlink.ui.activity.otc.contract.OtcChooseWalletContract
import com.stratagile.qlink.ui.activity.otc.presenter.OtcChooseWalletPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OtcChooseWalletActivity, provide field for OtcChooseWalletActivity
 * @date 2019/07/29 11:54:14
 */
@Module
class OtcChooseWalletModule (private val mView: OtcChooseWalletContract.View) {

    @Provides
    @ActivityScope
    fun provideOtcChooseWalletPresenter(httpAPIWrapper: HttpAPIWrapper) :OtcChooseWalletPresenter {
        return OtcChooseWalletPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOtcChooseWalletActivity() : OtcChooseWalletActivity {
        return mView as OtcChooseWalletActivity
    }
}