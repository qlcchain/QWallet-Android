package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.AssetListFragment;
import com.stratagile.qlink.ui.activity.wallet.contract.AssetListContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.AssetListPresenter;
import com.stratagile.qlink.ui.adapter.wallet.AssetListAdapter;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of AssetListFragment, provide field for AssetListFragment
 * @date 2018/01/18 20:42:28
 */
@Module
public class AssetListModule {
    private final AssetListContract.View mView;


    public AssetListModule(AssetListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public AssetListPresenter provideAssetListPresenter(HttpAPIWrapper httpAPIWrapper, AssetListFragment fragment) {
        return new AssetListPresenter(httpAPIWrapper, mView, fragment);
    }

    @Provides
    @ActivityScope
    public AssetListFragment provideAssetListFragment() {
        return (AssetListFragment) mView;
    }

    @Provides
    @ActivityScope
    public AssetListAdapter provideAssetListAdapter() {
        return new AssetListAdapter(new ArrayList<>());
    }
}