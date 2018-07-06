package com.stratagile.qlink.ui.activity.wordcup.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.WordCupIntroduceActivity;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupIntroduceContract;
import com.stratagile.qlink.ui.activity.wordcup.presenter.WordCupIntroducePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The moduele of WordCupIntroduceActivity, provide field for WordCupIntroduceActivity
 * @date 2018/06/11 13:50:44
 */
@Module
public class WordCupIntroduceModule {
    private final WordCupIntroduceContract.View mView;


    public WordCupIntroduceModule(WordCupIntroduceContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WordCupIntroducePresenter provideWordCupIntroducePresenter(HttpAPIWrapper httpAPIWrapper, WordCupIntroduceActivity mActivity) {
        return new WordCupIntroducePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WordCupIntroduceActivity provideWordCupIntroduceActivity() {
        return (WordCupIntroduceActivity) mView;
    }
}