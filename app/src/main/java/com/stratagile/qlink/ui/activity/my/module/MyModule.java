package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.MyFragment;
import com.stratagile.qlink.ui.activity.my.contract.MyContract;
import com.stratagile.qlink.ui.activity.my.presenter.MyPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of MyFragment, provide field for MyFragment
 * @date 2019/04/09 10:02:03
 */
@Module
public class MyModule {
    private final MyContract.View mView;


    public MyModule(MyContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public MyPresenter provideMyPresenter(HttpAPIWrapper httpAPIWrapper, MyFragment mFragment) {
        return new MyPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public MyFragment provideMyFragment() {
        return (MyFragment) mView;
    }
}