package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.OrderRecordActivity
import com.stratagile.qlink.ui.activity.otc.contract.OrderRecordContract
import com.stratagile.qlink.ui.activity.otc.presenter.OrderRecordPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of OrderRecordActivity, provide field for OrderRecordActivity
 * @date 2019/07/10 14:40:52
 */
@Module
class OrderRecordModule (private val mView: OrderRecordContract.View) {

    @Provides
    @ActivityScope
    fun provideOrderRecordPresenter(httpAPIWrapper: HttpAPIWrapper) :OrderRecordPresenter {
        return OrderRecordPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideOrderRecordActivity() : OrderRecordActivity {
        return mView as OrderRecordActivity
    }
}