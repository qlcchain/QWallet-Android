package com.stratagile.qlink.ui.activity.qlc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.QlcWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.qlc.contract.QlcWalletCreatedContract;
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcWalletCreatedPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of QlcWalletCreatedActivity, provide field for QlcWalletCreatedActivity
 * @date 2019/05/20 09:24:16
 */
@Module
public class QlcWalletCreatedModule {
    private final QlcWalletCreatedContract.View mView;


    public QlcWalletCreatedModule(QlcWalletCreatedContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public QlcWalletCreatedPresenter provideQlcWalletCreatedPresenter(HttpAPIWrapper httpAPIWrapper, QlcWalletCreatedActivity mActivity) {
        return new QlcWalletCreatedPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public QlcWalletCreatedActivity provideQlcWalletCreatedActivity() {
        return (QlcWalletCreatedActivity) mView;
    }
}