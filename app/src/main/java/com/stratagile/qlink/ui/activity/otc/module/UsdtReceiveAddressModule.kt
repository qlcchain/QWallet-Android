package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.UsdtReceiveAddressActivity
import com.stratagile.qlink.ui.activity.otc.contract.UsdtReceiveAddressContract
import com.stratagile.qlink.ui.activity.otc.presenter.UsdtReceiveAddressPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of UsdtReceiveAddressActivity, provide field for UsdtReceiveAddressActivity
 * @date 2019/07/17 16:50:34
 */
@Module
class UsdtReceiveAddressModule (private val mView: UsdtReceiveAddressContract.View) {

    @Provides
    @ActivityScope
    fun provideUsdtReceiveAddressPresenter(httpAPIWrapper: HttpAPIWrapper) :UsdtReceiveAddressPresenter {
        return UsdtReceiveAddressPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideUsdtReceiveAddressActivity() : UsdtReceiveAddressActivity {
        return mView as UsdtReceiveAddressActivity
    }
}