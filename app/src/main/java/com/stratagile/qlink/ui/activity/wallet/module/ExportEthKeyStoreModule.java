package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ExportEthKeyStoreActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.ExportEthKeyStoreContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.ExportEthKeyStorePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of ExportEthKeyStoreActivity, provide field for ExportEthKeyStoreActivity
 * @date 2018/11/06 17:23:06
 */
@Module
public class ExportEthKeyStoreModule {
    private final ExportEthKeyStoreContract.View mView;


    public ExportEthKeyStoreModule(ExportEthKeyStoreContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ExportEthKeyStorePresenter provideExportEthKeyStorePresenter(HttpAPIWrapper httpAPIWrapper, ExportEthKeyStoreActivity mActivity) {
        return new ExportEthKeyStorePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ExportEthKeyStoreActivity provideExportEthKeyStoreActivity() {
        return (ExportEthKeyStoreActivity) mView;
    }
}