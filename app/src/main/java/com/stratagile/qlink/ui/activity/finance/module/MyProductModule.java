package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.MyProductActivity;
import com.stratagile.qlink.ui.activity.finance.contract.MyProductContract;
import com.stratagile.qlink.ui.activity.finance.presenter.MyProductPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of MyProductActivity, provide field for MyProductActivity
 * @date 2019/04/11 16:18:23
 */
@Module
public class MyProductModule {
    private final MyProductContract.View mView;


    public MyProductModule(MyProductContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MyProductPresenter provideMyProductPresenter(HttpAPIWrapper httpAPIWrapper, MyProductActivity mActivity) {
        return new MyProductPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public MyProductActivity provideMyProductActivity() {
        return (MyProductActivity) mView;
    }
}