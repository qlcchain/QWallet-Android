package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.my.contract.AccountContract;
import com.stratagile.qlink.ui.activity.my.presenter.AccountPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of AccountActivity, provide field for AccountActivity
 * @date 2019/04/09 11:31:42
 */
@Module
public class AccountModule {
    private final AccountContract.View mView;


    public AccountModule(AccountContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public AccountPresenter provideAccountPresenter(HttpAPIWrapper httpAPIWrapper, AccountActivity mActivity) {
        return new AccountPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public AccountActivity provideAccountActivity() {
        return (AccountActivity) mView;
    }
}