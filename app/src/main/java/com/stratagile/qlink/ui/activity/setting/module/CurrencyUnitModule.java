package com.stratagile.qlink.ui.activity.setting.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.setting.CurrencyUnitActivity;
import com.stratagile.qlink.ui.activity.setting.contract.CurrencyUnitContract;
import com.stratagile.qlink.ui.activity.setting.presenter.CurrencyUnitPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: The moduele of CurrencyUnitActivity, provide field for CurrencyUnitActivity
 * @date 2018/10/29 14:00:24
 */
@Module
public class CurrencyUnitModule {
    private final CurrencyUnitContract.View mView;


    public CurrencyUnitModule(CurrencyUnitContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public CurrencyUnitPresenter provideCurrencyUnitPresenter(HttpAPIWrapper httpAPIWrapper, CurrencyUnitActivity mActivity) {
        return new CurrencyUnitPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public CurrencyUnitActivity provideCurrencyUnitActivity() {
        return (CurrencyUnitActivity) mView;
    }
}