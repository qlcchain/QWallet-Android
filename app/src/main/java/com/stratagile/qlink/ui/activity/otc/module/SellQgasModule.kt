package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.SellQgasActivity
import com.stratagile.qlink.ui.activity.otc.contract.SellQgasContract
import com.stratagile.qlink.ui.activity.otc.presenter.SellQgasPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of SellQgasActivity, provide field for SellQgasActivity
 * @date 2019/07/09 14:18:11
 */
@Module
class SellQgasModule (private val mView: SellQgasContract.View) {

    @Provides
    @ActivityScope
    fun provideSellQgasPresenter(httpAPIWrapper: HttpAPIWrapper) :SellQgasPresenter {
        return SellQgasPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSellQgasActivity() : SellQgasActivity {
        return mView as SellQgasActivity
    }
}