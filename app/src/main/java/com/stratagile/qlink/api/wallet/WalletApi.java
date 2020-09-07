package com.stratagile.qlink.api.wallet;



import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by huzhipeng on 2018/5/10.
 */

public class WalletApi {

    private static WalletApi instance;
    private CompositeDisposable mCompositeDisposable;

    /**
     * 获取单例
     * @return 返回交易工具类的实体
     */
    public static synchronized WalletApi getInstance() {
        if (instance == null) {
            instance = new WalletApi();
        }
        return instance;
    }

    private WalletApi() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
    }

}
