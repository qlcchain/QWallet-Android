package com.stratagile.qlink.ui.activity.mainwallet.module;

import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.mainwallet.MainTransactionRecordListFragment;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainTransactionRecordListContract;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainTransactionRecordListPresenter;
import com.stratagile.qlink.ui.adapter.mainwallet.MainTransactionRecordListAdapter;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: The moduele of MainTransactionRecordListFragment, provide field for MainTransactionRecordListFragment
 * @date 2018/06/13 20:52:12
 */
@Module
public class MainTransactionRecordListModule {
    private final MainTransactionRecordListContract.View mView;


    public MainTransactionRecordListModule(MainTransactionRecordListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MainTransactionRecordListPresenter provideMainTransactionRecordListPresenter(MainHttpAPIWrapper httpAPIWrapper, MainTransactionRecordListFragment fragment) {
        return new MainTransactionRecordListPresenter(httpAPIWrapper, mView, fragment);
    }

    @Provides
    @ActivityScope
    public MainTransactionRecordListFragment provideMainTransactionRecordListFragment() {
        return (MainTransactionRecordListFragment) mView;
    }

    @Provides
    @ActivityScope
    public MainTransactionRecordListAdapter provideMainTransactionRecordListAdapter() {
        return new MainTransactionRecordListAdapter(new ArrayList<>());
    }
}