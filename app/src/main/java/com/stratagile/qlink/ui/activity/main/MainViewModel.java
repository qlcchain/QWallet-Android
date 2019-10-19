package com.stratagile.qlink.ui.activity.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.stratagile.qlink.Account;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.otc.TradePair;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> qrcode = new MutableLiveData<>();

    public MutableLiveData<Long> timeStampAllWalletInitData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> tokens = new MutableLiveData<>();

    public MutableLiveData<AllWallet.WalletType> walletTypeMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<AllWallet> allWalletMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<TokenInfo>> tokenInfoMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Balance> balanceMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Long> timeStampLiveData = new MutableLiveData<>();

    public MutableLiveData<String> currentEntrustOrderType = new MutableLiveData<>();
    public MutableLiveData<String> refreshShowOrder = new MutableLiveData<>();

    public MutableLiveData<UserAccount> currentUserAccount = new MutableLiveData<>();

    public MutableLiveData<ArrayList<TradePair.PairsListBean>> pairsLiveData = new MutableLiveData<>();

    public MutableLiveData<Boolean> isBind = new MutableLiveData<>();

}
