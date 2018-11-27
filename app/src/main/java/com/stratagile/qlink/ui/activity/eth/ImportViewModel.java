package com.stratagile.qlink.ui.activity.eth;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ImportViewModel extends ViewModel {
    public MutableLiveData<String> walletAddress = new MutableLiveData<>();
    public MutableLiveData<String> qrCode = new MutableLiveData<>();
}
