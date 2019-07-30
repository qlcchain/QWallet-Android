package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.BuyQgasActivity
import com.stratagile.qlink.ui.activity.otc.contract.BuyQgasContract
import com.stratagile.qlink.ui.activity.otc.presenter.BuyQgasPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of BuyQgasActivity, provide field for BuyQgasActivity
 * @date 2019/07/09 14:18:25
 */
@Module
class BuyQgasModule (private val mView: BuyQgasContract.View) {

    @Provides
    @ActivityScope
    fun provideBuyQgasPresenter(httpAPIWrapper: HttpAPIWrapper) :BuyQgasPresenter {
        return BuyQgasPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideBuyQgasActivity() : BuyQgasActivity {
        return mView as BuyQgasActivity
    }
}