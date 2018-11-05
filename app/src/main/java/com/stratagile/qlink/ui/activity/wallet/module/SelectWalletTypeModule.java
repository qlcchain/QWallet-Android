package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.SelectWalletTypeActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.SelectWalletTypeContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.SelectWalletTypePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of SelectWalletTypeActivity, provide field for SelectWalletTypeActivity
 * @date 2018/10/19 10:51:45
 */
@Module
public class SelectWalletTypeModule {
    private final SelectWalletTypeContract.View mView;


    public SelectWalletTypeModule(SelectWalletTypeContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SelectWalletTypePresenter provideSelectWalletTypePresenter(HttpAPIWrapper httpAPIWrapper, SelectWalletTypeActivity mActivity) {
        return new SelectWalletTypePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SelectWalletTypeActivity provideSelectWalletTypeActivity() {
        return (SelectWalletTypeActivity) mView;
    }
}