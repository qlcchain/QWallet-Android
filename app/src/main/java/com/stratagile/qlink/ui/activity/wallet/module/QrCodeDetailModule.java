package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.QrCodeDetailActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.QrCodeDetailContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.QrCodeDetailPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of QrCodeDetailActivity, provide field for QrCodeDetailActivity
 * @date 2018/02/28 15:16:22
 */
@Module
public class QrCodeDetailModule {
    private final QrCodeDetailContract.View mView;


    public QrCodeDetailModule(QrCodeDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public QrCodeDetailPresenter provideQrCodeDetailPresenter(HttpAPIWrapper httpAPIWrapper, QrCodeDetailActivity mActivity) {
        return new QrCodeDetailPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public QrCodeDetailActivity provideQrCodeDetailActivity() {
        return (QrCodeDetailActivity) mView;
    }
}