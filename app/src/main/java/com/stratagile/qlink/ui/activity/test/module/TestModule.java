package com.stratagile.qlink.ui.activity.test.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.test.TestFragment;
import com.stratagile.qlink.ui.activity.test.contract.TestContract;
import com.stratagile.qlink.ui.activity.test.presenter.TestPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.test
 * @Description: The moduele of TestFragment, provide field for TestFragment
 * @date 2018/09/10 16:51:54
 */
@Module
public class TestModule {
    private final TestContract.View mView;


    public TestModule(TestContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public TestPresenter provideTestPresenter(HttpAPIWrapper httpAPIWrapper, TestFragment mFragment) {
        return new TestPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public TestFragment provideTestFragment() {
        return (TestFragment) mView;
    }
}