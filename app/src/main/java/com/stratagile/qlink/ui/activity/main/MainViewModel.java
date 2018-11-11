package com.stratagile.qlink.ui.activity.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.stratagile.qlink.entity.AllWallet;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> qrcode = new MutableLiveData<>();

    public MutableLiveData<ArrayList<String>> tokens = new MutableLiveData<>();

    public MutableLiveData<AllWallet.WalletType> walletTypeMutableLiveData = new MutableLiveData<>();
}
