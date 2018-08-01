package com.stratagile.qlink.ui.activity.rank;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class RankViewModel extends ViewModel {
    private MutableLiveData<Integer> currentPage = new MutableLiveData<>();

    public MutableLiveData<Integer> getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(MutableLiveData<Integer> currentPage) {
        this.currentPage = currentPage;
    }
}
