package com.stratagile.qlink.api;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpObserver<T> implements Observer<T> {
    Disposable disposable;
    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onError(Throwable e) {
        disposable.dispose();
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        disposable.dispose();
    }
}
