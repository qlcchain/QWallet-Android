package com.stratagile.qlink.ui.activity.qlc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcPrivateKeyFragment;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcPrivateKeyContract;
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcPrivateKeyPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of ImportQlcPrivateKeyFragment, provide field for ImportQlcPrivateKeyFragment
 * @date 2019/05/21 09:46:08
 */
@Module
public class ImportQlcPrivateKeyModule {
    private final ImportQlcPrivateKeyContract.View mView;


    public ImportQlcPrivateKeyModule(ImportQlcPrivateKeyContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ImportQlcPrivateKeyPresenter provideImportQlcPrivateKeyPresenter(HttpAPIWrapper httpAPIWrapper, ImportQlcPrivateKeyFragment mFragment) {
        return new ImportQlcPrivateKeyPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public ImportQlcPrivateKeyFragment provideImportQlcPrivateKeyFragment() {
        return (ImportQlcPrivateKeyFragment) mView;
    }
}