package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.NewOrderActivity
import com.stratagile.qlink.ui.activity.otc.contract.NewOrderContract
import com.stratagile.qlink.ui.activity.otc.presenter.NewOrderPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of NewOrderActivity, provide field for NewOrderActivity
 * @date 2019/07/08 16:00:52
 */
@Module
class NewOrderModule (private val mView: NewOrderContract.View) {

    @Provides
    @ActivityScope
    fun provideNewOrderPresenter(httpAPIWrapper: HttpAPIWrapper) :NewOrderPresenter {
        return NewOrderPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideNewOrderActivity() : NewOrderActivity {
        return mView as NewOrderActivity
    }
}