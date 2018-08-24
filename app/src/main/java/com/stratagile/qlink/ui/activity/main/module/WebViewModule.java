package com.stratagile.qlink.ui.activity.main.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.WebViewActivity;
import com.stratagile.qlink.ui.activity.main.contract.WebViewContract;
import com.stratagile.qlink.ui.activity.main.presenter.WebViewPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The moduele of WebViewActivity, provide field for WebViewActivity
 * @date 2018/05/30 11:52:27
 */
@Module
public class WebViewModule {
    private final WebViewContract.View mView;


    public WebViewModule(WebViewContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WebViewPresenter provideWebViewPresenter(HttpAPIWrapper httpAPIWrapper, WebViewActivity mActivity) {
        return new WebViewPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WebViewActivity provideWebViewActivity() {
        return (WebViewActivity) mView;
    }
}