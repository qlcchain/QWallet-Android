package com.stratagile.qlink.ui.activity.wordcup.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.WordCupBetActivity;
import com.stratagile.qlink.ui.activity.wordcup.module.WordCupBetModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The component for WordCupBetActivity
 * @date 2018/06/12 11:36:18
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WordCupBetModule.class)
public interface WordCupBetComponent {
    WordCupBetActivity inject(WordCupBetActivity Activity);
}