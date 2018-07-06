package com.stratagile.qlink.ui.activity.wordcup.presenter;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.socks.library.KLog;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.data.api.MainHttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.entity.ServerTime;
import com.stratagile.qlink.ui.activity.wordcup.contract.GameListContract;
import com.stratagile.qlink.ui.activity.wordcup.GameListFragment;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: presenter of GameListFragment
 * @date 2018/06/11 16:31:05
 */
public class GameListPresenter implements GameListContract.GameListContractPresenter{

    MainHttpAPIWrapper httpAPIWrapper;
    private final GameListContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private GameListFragment mFragment;

    private String currentServerTime = "";

    @Inject
    public GameListPresenter(@NonNull MainHttpAPIWrapper httpAPIWrapper, @NonNull GameListContract.View view, GameListFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
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

    @Override
    public void getGameListFromServer(String gameStatus, int requestPage, int onePageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("pageSize", onePageSize + "");
        map.put("pageNum", requestPage + "");

    }

    @Override
    public void getRaceTimes(Map map) {
        Disposable disposable = httpAPIWrapper.getRaceTimes(map)
                .subscribe(new Consumer<RaceTimes>() {
                    @Override
                    public void accept(RaceTimes raceTimes) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.showRaceTimes(raceTimes.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getCurrentServerTime(Map map) {
        Disposable disposable = httpAPIWrapper.getServerTime(map)
                .subscribe(new Consumer<ServerTime>() {
                    @Override
                    public void accept(ServerTime serverTime) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        currentServerTime = serverTime.getData().getSysTime();
                        mView.setCurrentServerTime(currentServerTime);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    public void isCanBetThisRace(String time, int position) {
        mView.showProgressDialog();
        Disposable disposable = httpAPIWrapper.getServerTime(new HashMap())
                .subscribe(new Consumer<ServerTime>() {
                    @Override
                    public void accept(ServerTime serverTime) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.closeProgressDialog();
                        currentServerTime = serverTime.getData().getSysTime();
                        if (isCanBet(time)) {
                            mView.startBet(position);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mActivity, mActivity.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private boolean isCanBet(String time) {
        SimpleDateFormat sysSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date sysDate;
        SimpleDateFormat raceSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date raceDate;
        try {
            sysDate = sysSdf.parse(currentServerTime);
            Calendar sysCalendar = Calendar.getInstance();
            sysCalendar.setTime(sysDate);
            raceDate = raceSdf.parse("2018-" + time);
            Calendar raceCalendar = Calendar.getInstance();
            raceCalendar.setTime(raceDate);
            if (sysCalendar.getTimeInMillis() - raceCalendar.getTimeInMillis() > 1000*60*30) {
                //超过30分钟，不能再下注
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}