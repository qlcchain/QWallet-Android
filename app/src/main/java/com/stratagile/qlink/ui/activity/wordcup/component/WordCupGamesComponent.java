package com.stratagile.qlink.ui.activity.wordcup.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.WordCupGamesActivity;
import com.stratagile.qlink.ui.activity.wordcup.module.WordCupGamesModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The component for WordCupGamesActivity
 * @date 2018/06/11 14:38:56
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WordCupGamesModule.class)
public interface WordCupGamesComponent {
    WordCupGamesActivity inject(WordCupGamesActivity Activity);
}