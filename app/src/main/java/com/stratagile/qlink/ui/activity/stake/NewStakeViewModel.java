package com.stratagile.qlink.ui.activity.stake;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.stratagile.qlink.entity.stake.LockResult;

public class NewStakeViewModel extends ViewModel {
    public MutableLiveData<LockResult> lockResult = new MutableLiveData<>();

    public MutableLiveData<String> txidMutableLiveData = new MutableLiveData<>();
}
