package com.stratagile.qlink.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.utils.TimeUtil;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class TimeView extends LinearLayout {
    private TextView day;
    private TextView hour;
    private TextView minute;
    private TextView second;
    private long newTime = 0;
    private Disposable mDisposable;

    private MiaoshaWanchengListener miaoshaWanchengListener;

    public MiaoshaWanchengListener getMiaoshaWanchengListener() {
        return miaoshaWanchengListener;
    }

    public void setMiaoshaWanchengListener(MiaoshaWanchengListener miaoshaWanchengListener) {
        this.miaoshaWanchengListener = miaoshaWanchengListener;
    }

    public long getNewTime() {
        return newTime;
    }

    public void setNewTime(long newTime) {
        this.newTime = newTime;
    }

    public TimeView(Context context) {
        super(context);
        init(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.time_view, this, true);
        day = view.findViewById(R.id.day);
        hour = view.findViewById(R.id.hour);
        minute = view.findViewById(R.id.minute);
        second = view.findViewById(R.id.second);
    }

    private void update() {

    }

    public void setRemainTime(long remainTime, long currentTime) {
        newTime = remainTime;
        long remainTimeStamp = (remainTime - currentTime)/1000;
        if (remainTimeStamp <= 0) {
            if (miaoshaWanchengListener != null) {
                miaoshaWanchengListener.onMiaoshaComplete();
            }
            return;
        }
        if (remainTimeStamp < 0 ) {

        }
        Observable.interval(0, 1, TimeUnit.SECONDS).take(remainTimeStamp + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return remainTimeStamp - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//发射用的是observeOn
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        KLog.i("1");
                        mDisposable = disposable;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        KLog.i("2");
                    }

                    @Override
                    public void onNext(@NonNull Long remainTime) {
                        setTime(remainTime);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        KLog.i("4");
                    }

                    @Override
                    public void onComplete() {
                        KLog.i("秒杀完成");
                        if (miaoshaWanchengListener != null) {
                            miaoshaWanchengListener.onMiaoshaComplete();
                        }
                    }
                });

    }

    private void setTime(long defferTime) {
//        KLog.i(defferTime);
        long hour = defferTime / 60 / 60;                 //小时
        long day = hour / 24;                           //剩余天数
        long remainSecond = (defferTime - 60* 60 *hour);      //除以小时之后剩余的秒数
        long minute = remainSecond / 60;                 //去掉小时之后剩下的分钟数
        long second = remainSecond - 60*minute;                 //剩余的秒数
        hour = hour % 24;
        if (day < 10) {
            this.day.setText("0" + day);
        } else {
            this.day.setText(day + "");
        }
        if (hour < 10) {
            this.hour.setText("0" + hour);
        } else {
            this.hour.setText(hour + "");
        }
        if (minute < 10) {
            this.minute.setText("0" + minute);
        } else {
            this.minute.setText(minute + "");
        }
        if (second < 10) {
            this.second.setText("0" + second);
        } else {
            this.second.setText(second + "");
        }
    }

    public interface MiaoshaWanchengListener {
        void onMiaoshaComplete();
    }

    public void onFinish() {
        mDisposable.dispose();
    }
}
