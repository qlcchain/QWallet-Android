package com.stratagile.qlink.ui.activity.wordcup.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.WordCupGamesActivity;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupGamesContract;
import com.stratagile.qlink.ui.activity.wordcup.presenter.WordCupGamesPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The moduele of WordCupGamesActivity, provide field for WordCupGamesActivity
 * @date 2018/06/11 14:38:56
 */
@Module
public class WordCupGamesModule {
    private final WordCupGamesContract.View mView;


    public WordCupGamesModule(WordCupGamesContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WordCupGamesPresenter provideWordCupGamesPresenter(HttpAPIWrapper httpAPIWrapper, WordCupGamesActivity mActivity) {
        return new WordCupGamesPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WordCupGamesActivity provideWordCupGamesActivity() {
        return (WordCupGamesActivity) mView;
    }
}