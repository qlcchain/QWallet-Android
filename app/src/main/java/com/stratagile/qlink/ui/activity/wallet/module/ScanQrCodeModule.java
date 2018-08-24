package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ScanQrCodeContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ScanQrCodePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ScanQrCodeActivity, provide field for ScanQrCodeActivity
 * @date 2018/03/05 17:42:26
 */
@Module
public class ScanQrCodeModule {
    private final ScanQrCodeContract.View mView;


    public ScanQrCodeModule(ScanQrCodeContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ScanQrCodePresenter provideScanQrCodePresenter(HttpAPIWrapper httpAPIWrapper, ScanQrCodeActivity mActivity) {
        return new ScanQrCodePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ScanQrCodeActivity provideScanQrCodeActivity() {
        return (ScanQrCodeActivity) mView;
    }
}