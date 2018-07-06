package com.stratagile.qlink.ui.activity.wordcup.presenter;
import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupGamesContract;
import com.stratagile.qlink.ui.activity.wordcup.WordCupGamesActivity;

import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: presenter of WordCupGamesActivity
 * @date 2018/06/11 14:38:56
 */
public class WordCupGamesPresenter implements WordCupGamesContract.WordCupGamesContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final WordCupGamesContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private WordCupGamesActivity mActivity;

    @Inject
    public WordCupGamesPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull WordCupGamesContract.View view, WordCupGamesActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
             mCompositeDisposable.dispose();
        }
    }

}