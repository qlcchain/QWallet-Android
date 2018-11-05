package com.stratagile.qlink.ui.activity.eth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthWatchComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthWatchContract;
import com.stratagile.qlink.ui.activity.eth.module.EthWatchModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWatchPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/22 14:16:26
 */

public class EthWatchFragment extends BaseFragment implements EthWatchContract.View {

    @Inject
    EthWatchPresenter mPresenter;
    private static final String ARG_TYPE = "arg_type";

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_eth_watch, null);
       ButterKnife.bind(this, view);
       Bundle mBundle = getArguments();
       return view;
   }

    public static EthWatchFragment newInstance(String param) {
        EthWatchFragment fragment = new EthWatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void setupFragmentComponent() {
       DaggerEthWatchComponent
               .builder()
               .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
               .ethWatchModule(new EthWatchModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(EthWatchContract.EthWatchContractPresenter presenter) {
        mPresenter = (EthWatchPresenter) presenter;
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