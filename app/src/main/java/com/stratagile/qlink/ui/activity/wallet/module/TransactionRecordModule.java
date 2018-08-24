package com.stratagile.qlink.ui.activity.wallet.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.TransactionRecordActivity;
import com.stratagile.qlink.ui.activity.wallet.contract.TransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.presenter.TransactionRecordPresenter;
import com.stratagile.qlink.ui.adapter.wallet.TransactionRecordAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The moduele of TransactionRecordActivity, provide field for TransactionRecordActivity
 * @date 2018/01/26 17:16:57
 */
@Module
public class TransactionRecordModule {
    private final TransactionRecordContract.View mView;


    public TransactionRecordModule(TransactionRecordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public TransactionRecordPresenter provideTransactionRecordPresenter(HttpAPIWrapper httpAPIWrapper, TransactionRecordActivity mActivity) {
        return new TransactionRecordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public TransactionRecordActivity provideTransactionRecordActivity() {
        return (TransactionRecordActivity) mView;
    }

    @Provides
    @ActivityScope
    public TransactionRecordAdapter provideTransactionRecordAdapter() {
        return new TransactionRecordAdapter(new ArrayList<>());
    }
}