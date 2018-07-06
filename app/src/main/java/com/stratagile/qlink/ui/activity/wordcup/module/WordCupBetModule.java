package com.stratagile.qlink.ui.activity.wordcup.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.WordCupBetActivity;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupBetContract;
import com.stratagile.qlink.ui.activity.wordcup.presenter.WordCupBetPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The moduele of WordCupBetActivity, provide field for WordCupBetActivity
 * @date 2018/06/12 11:36:18
 */
@Module
public class WordCupBetModule {
    private final WordCupBetContract.View mView;


    public WordCupBetModule(WordCupBetContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WordCupBetPresenter provideWordCupBetPresenter(MainHttpAPIWrapper httpAPIWrapper, WordCupBetActivity mActivity) {
        return new WordCupBetPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public WordCupBetActivity provideWordCupBetActivity() {
        return (WordCupBetActivity) mView;
    }
}