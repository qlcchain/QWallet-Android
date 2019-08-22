package com.stratagile.qlink.ui.activity.otc.module

import com.stratagile.qlink.data.api.HttpAPIWrapper
import com.stratagile.qlink.ui.activity.base.ActivityScope
import com.stratagile.qlink.ui.activity.otc.SelectCurrencyActivity
import com.stratagile.qlink.ui.activity.otc.contract.SelectCurrencyContract
import com.stratagile.qlink.ui.activity.otc.presenter.SelectCurrencyPresenter

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.otc
 * @Description: The moduele of SelectCurrencyActivity, provide field for SelectCurrencyActivity
 * @date 2019/08/19 15:22:57
 */
@Module
class SelectCurrencyModule (private val mView: SelectCurrencyContract.View) {

    @Provides
    @ActivityScope
    fun provideSelectCurrencyPresenter(httpAPIWrapper: HttpAPIWrapper) :SelectCurrencyPresenter {
        return SelectCurrencyPresenter(httpAPIWrapper, mView)
    }

    @Provides
    @ActivityScope
    fun provideSelectCurrencyActivity() : SelectCurrencyActivity {
        return mView as SelectCurrencyActivity
    }
}