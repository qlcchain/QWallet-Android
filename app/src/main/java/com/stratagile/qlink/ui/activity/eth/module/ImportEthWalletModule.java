package com.stratagile.qlink.ui.activity.eth.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.eth.ImportEthWalletActivity;
import com.stratagile.qlink.ui.activity.eth.contract.ImportEthWalletContract;
import com.stratagile.qlink.ui.activity.eth.presenter.ImportEthWalletPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: The moduele of ImportEthWalletActivity, provide field for ImportEthWalletActivity
 * @date 2018/05/24 16:30:41
 */
@Module
public class ImportEthWalletModule {
    private final ImportEthWalletContract.View mView;


    public ImportEthWalletModule(ImportEthWalletContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ImportEthWalletPresenter provideImportEthWalletPresenter(HttpAPIWrapper httpAPIWrapper, ImportEthWalletActivity mActivity) {
        return new ImportEthWalletPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ImportEthWalletActivity provideImportEthWalletActivity() {
        return (ImportEthWalletActivity) mView;
    }
}