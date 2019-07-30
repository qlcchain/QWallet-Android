package com.stratagile.qlink.ui.activity.qlc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcSeedFragment;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcSeedContract;
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcSeedPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of ImportQlcSeedFragment, provide field for ImportQlcSeedFragment
 * @date 2019/05/21 09:46:27
 */
@Module
public class ImportQlcSeedModule {
    private final ImportQlcSeedContract.View mView;


    public ImportQlcSeedModule(ImportQlcSeedContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ImportQlcSeedPresenter provideImportQlcSeedPresenter(HttpAPIWrapper httpAPIWrapper, ImportQlcSeedFragment mFragment) {
        return new ImportQlcSeedPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public ImportQlcSeedFragment provideImportQlcSeedFragment() {
        return (ImportQlcSeedFragment) mView;
    }
}