package com.stratagile.qlink.ui.activity.rank;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.socks.library.KLog;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.entity.Active;
import com.stratagile.qlink.entity.ActiveList;
import com.stratagile.qlink.entity.BaseBack;

import java.util.HashMap;
import java.util.Map;

public class RankViewModel extends ViewModel {
    private MutableLiveData<String> currentAct = new MutableLiveData<>();
    private MutableLiveData<Active> active = new MutableLiveData<>();
    public MutableLiveData<ActiveList> activeListMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Active> getActive() {
        return active;
    }

    public void setActive(MutableLiveData<Active> active) {
        this.active = active;
    }

    public MutableLiveData<String> getCurrentAct() {
        return currentAct;
    }

    /**
     * 获取当前的活动
     */
    public void getAct() {
        AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().getAct(new HashMap<String, String>()).subscribe(new HttpObserver<Active>() {
            @Override
            public void onNext(Active baseBack) {
                active.setValue(baseBack);
                currentAct.setValue(baseBack.getData().getActs().get(0).getActId());
            }
        });
    }

    /**
     * 获取当前活动的资产
     */
    public void getActAssets(String actId) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("actId", actId);
        AppConfig.getInstance().getApplicationComponent().getHttpApiWrapper().getActAsset(infoMap).subscribe(new HttpObserver<ActiveList>() {
            @Override
            public void onNext(ActiveList baseBack) {
                activeListMutableLiveData.setValue(baseBack);
            }
        });
    }
}
