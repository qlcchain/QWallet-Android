package com.stratagile.qlink.ui.activity.wordcup.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.OpenMainWalletFragment;
import com.stratagile.qlink.ui.activity.wordcup.module.OpenMainWalletModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The component for OpenMainWalletFragment
 * @date 2018/06/13 17:37:05
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = OpenMainWalletModule.class)
public interface OpenMainWalletComponent {
    OpenMainWalletFragment inject(OpenMainWalletFragment Fragment);
}