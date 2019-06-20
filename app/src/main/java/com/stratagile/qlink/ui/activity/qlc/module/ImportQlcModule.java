package com.stratagile.qlink.ui.activity.qlc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcActivity;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcContract;
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of ImportQlcActivity, provide field for ImportQlcActivity
 * @date 2019/05/21 09:40:38
 */
@Module
public class ImportQlcModule {
    private final ImportQlcContract.View mView;


    public ImportQlcModule(ImportQlcContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ImportQlcPresenter provideImportQlcPresenter(HttpAPIWrapper httpAPIWrapper, ImportQlcActivity mActivity) {
        return new ImportQlcPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ImportQlcActivity provideImportQlcActivity() {
        return (ImportQlcActivity) mView;
    }
}