package com.stratagile.qlink.ui.activity.topup.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.topup.VoucherDetailActivity
import com.stratagile.qlink.ui.activity.topup.contract.VoucherDetailContract
import com.stratagile.qlink.ui.activity.topup.presenter.VoucherDetailPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.topup
 * @Description: The moduele of VoucherDetailActivity, provide field for VoucherDetailActivity
 * @date 2019/10/17 17:29:37
 */
@Module
class VoucherDetailModule (private val mView: VoucherDetailContract.View) {

    @Provides
    @ActivityScope
    fun provideVoucherDetailPresenter(httpAPIWrapper: HttpAPIWrapper) :VoucherDetailPresenter {
        return VoucherDetailPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideVoucherDetailActivity() : VoucherDetailActivity {
        return mView as VoucherDetailActivity
    }
}