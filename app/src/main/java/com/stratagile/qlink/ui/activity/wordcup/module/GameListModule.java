package com.stratagile.qlink.ui.activity.wordcup.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wordcup.GameListFragment;
import com.stratagile.qlink.ui.activity.wordcup.contract.GameListContract;
import com.stratagile.qlink.ui.activity.wordcup.presenter.GameListPresenter;
import com.stratagile.qlink.ui.adapter.wordcup.GameListAdapter;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: The moduele of GameListFragment, provide field for GameListFragment
 * @date 2018/06/11 16:31:05
 */
@Module
public class GameListModule {
    private final GameListContract.View mView;


    public GameListModule(GameListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public GameListPresenter provideGameListPresenter(MainHttpAPIWrapper httpAPIWrapper, GameListFragment fragment) {
        return new GameListPresenter(httpAPIWrapper, mView, fragment);
    }

    @Provides
    @ActivityScope
    public GameListFragment provideGameListFragment() {
        return (GameListFragment) mView;
    }

    @Provides
    @ActivityScope
    public GameListAdapter provideGameListAdapter() {
        return new GameListAdapter(new ArrayList<>());
    }
}