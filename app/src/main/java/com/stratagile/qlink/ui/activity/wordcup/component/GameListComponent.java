package com.stratagile.qlink.ui.activity.wordcup.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.GameListFragment;
import com.stratagile.qlink.ui.activity.wordcup.module.GameListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The component for GameListFragment
 * @date 2018/06/11 16:31:05
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = GameListModule.class)
public interface GameListComponent {
    GameListFragment inject(GameListFragment Fragment);
}