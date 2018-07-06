package com.stratagile.qlink.ui.activity.base;

import android.os.Bundle;

import com.socks.library.KLog;
import com.stratagile.qlink.base.BaseFragment;

/**
 * @author: Yuan.Y.Q
 * @date: 2017/7/12
 * @descprition:
 */

public abstract class MyBaseFragment extends BaseFragment {
    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
//        KLog.i("Second","MyBaseActivityCreated");
        KLog.i("Second","MyBaseActivityCreated is Visible "+ isVisibleToUser);
    }

    public abstract void fetchData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
}
