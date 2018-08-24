package com.stratagile.qlink.ui.activity.wifi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerWifiShareComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiShareContract;
import com.stratagile.qlink.ui.activity.wifi.module.WifiShareModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiSharePresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/15 11:52:51
 */

public class WifiShareFragment extends BaseFragment implements WifiShareContract.View {

    @Inject
    WifiSharePresenter mPresenter;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_wifi_share, null);
       ButterKnife.bind(this, view);
       Bundle mBundle = getArguments();
       return view;
   }


    @Override
    protected void setupFragmentComponent() {
       DaggerWifiShareComponent
               .builder()
               .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
               .wifiShareModule(new WifiShareModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(WifiShareContract.WifiShareContractPresenter presenter) {
        mPresenter = (WifiSharePresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}