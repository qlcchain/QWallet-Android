package com.stratagile.qlink.ui.activity.wordcup.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.WordCupIntroduceActivity;
import com.stratagile.qlink.ui.activity.wordcup.module.WordCupIntroduceModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The component for WordCupIntroduceActivity
 * @date 2018/06/11 13:50:44
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WordCupIntroduceModule.class)
public interface WordCupIntroduceComponent {
    WordCupIntroduceActivity inject(WordCupIntroduceActivity Activity);
}