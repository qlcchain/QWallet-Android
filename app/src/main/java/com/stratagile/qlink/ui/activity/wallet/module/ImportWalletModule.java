package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ImportWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ImportWalletContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ImportWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ImportWalletActivity, provide field for ImportWalletActivity
 * @date 2018/01/23 14:24:49
 */
@Module
public class ImportWalletModule {
    private final ImportWalletContract.View mView;


    public ImportWalletModule(ImportWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ImportWalletPresenter provideImportWalletPresenter(HttpAPIWrapper httpAPIWrapper, ImportWalletActivity mActivity) {
        return new ImportWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ImportWalletActivity provideImportWalletActivity() {
        return (ImportWalletActivity) mView;
    }
}