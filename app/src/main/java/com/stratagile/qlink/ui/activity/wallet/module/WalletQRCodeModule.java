package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.WalletQRCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletQRCodeContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletQRCodePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of WalletQRCodeActivity, provide field for WalletQRCodeActivity
 * @date 2018/10/30 15:54:27
 */
@Module
public class WalletQRCodeModule {
    private final WalletQRCodeContract.View mView;


    public WalletQRCodeModule(WalletQRCodeContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WalletQRCodePresenter provideWalletQRCodePresenter(HttpAPIWrapper httpAPIWrapper, WalletQRCodeActivity mActivity) {
        return new WalletQRCodePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WalletQRCodeActivity provideWalletQRCodeActivity() {
        return (WalletQRCodeActivity) mView;
    }
}