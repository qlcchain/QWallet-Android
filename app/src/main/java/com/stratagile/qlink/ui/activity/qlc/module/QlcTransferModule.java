package com.stratagile.qlink.ui.activity.qlc.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.qlc.QlcTransferActivity;
import com.stratagile.qlink.ui.activity.qlc.contract.QlcTransferContract;
import com.stratagile.qlink.ui.activity.qlc.presenter.QlcTransferPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: The moduele of QlcTransferActivity, provide field for QlcTransferActivity
 * @date 2019/05/20 18:05:32
 */
@Module
public class QlcTransferModule {
    private final QlcTransferContract.View mView;


    public QlcTransferModule(QlcTransferContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public QlcTransferPresenter provideQlcTransferPresenter(HttpAPIWrapper httpAPIWrapper, QlcTransferActivity mActivity) {
        return new QlcTransferPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public QlcTransferActivity provideQlcTransferActivity() {
        return (QlcTransferActivity) mView;
    }
}