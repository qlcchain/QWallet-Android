package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.VerifyWalletPasswordContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.VerifyWalletPasswordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of VerifyWalletPasswordActivity, provide field for VerifyWalletPasswordActivity
 * @date 2018/01/24 18:19:18
 */
@Module
public class VerifyWalletPasswordModule {
    private final VerifyWalletPasswordContract.View mView;


    public VerifyWalletPasswordModule(VerifyWalletPasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public VerifyWalletPasswordPresenter provideVerifyWalletPasswordPresenter(HttpAPIWrapper httpAPIWrapper, VerifyWalletPasswordActivity mActivity) {
        return new VerifyWalletPasswordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public VerifyWalletPasswordActivity provideVerifyWalletPasswordActivity() {
        return (VerifyWalletPasswordActivity) mView;
    }
}