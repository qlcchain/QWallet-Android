package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.CreateWalletPasswordContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.CreateWalletPasswordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of CreateWalletPasswordActivity, provide field for CreateWalletPasswordActivity
 * @date 2018/01/24 17:48:46
 */
@Module
public class CreateWalletPasswordModule {
    private final CreateWalletPasswordContract.View mView;


    public CreateWalletPasswordModule(CreateWalletPasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public CreateWalletPasswordPresenter provideCreateWalletPasswordPresenter(HttpAPIWrapper httpAPIWrapper, CreateWalletPasswordActivity mActivity) {
        return new CreateWalletPasswordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public CreateWalletPasswordActivity provideCreateWalletPasswordActivity() {
        return (CreateWalletPasswordActivity) mView;
    }
}