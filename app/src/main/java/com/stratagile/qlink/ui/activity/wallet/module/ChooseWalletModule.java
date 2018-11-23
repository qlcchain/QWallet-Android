package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ChooseWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ChooseWalletContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChooseWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ChooseWalletActivity, provide field for ChooseWalletActivity
 * @date 2018/11/06 11:21:13
 */
@Module
public class ChooseWalletModule {
    private final ChooseWalletContract.View mView;


    public ChooseWalletModule(ChooseWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ChooseWalletPresenter provideChooseWalletPresenter(HttpAPIWrapper httpAPIWrapper, ChooseWalletActivity mActivity) {
        return new ChooseWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ChooseWalletActivity provideChooseWalletActivity() {
        return (ChooseWalletActivity) mView;
    }
}