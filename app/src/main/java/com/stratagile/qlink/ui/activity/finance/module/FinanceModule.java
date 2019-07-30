package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.FinanceFragment;
import com.stratagile.qlink.ui.activity.finance.contract.FinanceContract;
import com.stratagile.qlink.ui.activity.finance.presenter.FinancePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of FinanceFragment, provide field for FinanceFragment
 * @date 2019/04/08 17:36:49
 */
@Module
public class FinanceModule {
    private final FinanceContract.View mView;


    public FinanceModule(FinanceContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public FinancePresenter provideFinancePresenter(HttpAPIWrapper httpAPIWrapper, FinanceFragment mFragment) {
        return new FinancePresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public FinanceFragment provideFinanceFragment() {
        return (FinanceFragment) mView;
    }
}