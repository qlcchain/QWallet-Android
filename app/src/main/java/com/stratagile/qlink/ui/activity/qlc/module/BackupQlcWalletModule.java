package com.stratagile.qlink.ui.activity.qlc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.BackupQlcWalletActivity;
import com.stratagile.qlink.ui.activity.qlc.contract.BackupQlcWalletContract;
import com.stratagile.qlink.ui.activity.qlc.presenter.BackupQlcWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of BackupQlcWalletActivity, provide field for BackupQlcWalletActivity
 * @date 2019/05/20 10:52:55
 */
@Module
public class BackupQlcWalletModule {
    private final BackupQlcWalletContract.View mView;


    public BackupQlcWalletModule(BackupQlcWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public BackupQlcWalletPresenter provideBackupQlcWalletPresenter(HttpAPIWrapper httpAPIWrapper, BackupQlcWalletActivity mActivity) {
        return new BackupQlcWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public BackupQlcWalletActivity provideBackupQlcWalletActivity() {
        return (BackupQlcWalletActivity) mView;
    }
}