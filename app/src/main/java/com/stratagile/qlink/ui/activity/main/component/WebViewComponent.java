package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.WebViewActivity;
import com.stratagile.qlink.ui.activity.main.module.WebViewModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for WebViewActivity
 * @date 2018/05/30 11:52:27
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WebViewModule.class)
public interface WebViewComponent {
    WebViewActivity inject(WebViewActivity Activity);
}