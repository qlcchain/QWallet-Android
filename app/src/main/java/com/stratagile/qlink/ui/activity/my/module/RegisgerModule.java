package com.stratagile.qlink.ui.activity.my.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.RegisgerActivity;
import com.stratagile.qlink.ui.activity.my.contract.RegisgerContract;
import com.stratagile.qlink.ui.activity.my.presenter.RegisgerPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The moduele of RegisgerActivity, provide field for RegisgerActivity
 * @date 2019/04/23 12:02:02
 */
@Module
public class RegisgerModule {
    private final RegisgerContract.View mView;


    public RegisgerModule(RegisgerContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegisgerPresenter provideRegisgerPresenter(HttpAPIWrapper httpAPIWrapper, RegisgerActivity mActivity) {
        return new RegisgerPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RegisgerActivity provideRegisgerActivity() {
        return (RegisgerActivity) mView;
    }
}