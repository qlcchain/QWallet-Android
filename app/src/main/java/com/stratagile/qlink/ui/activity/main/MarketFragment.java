package com.stratagile.qlink.ui.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.main.component.DaggerMarketComponent;
import com.stratagile.qlink.ui.activity.main.contract.MarketContract;
import com.stratagile.qlink.ui.activity.main.module.MarketModule;
import com.stratagile.qlink.ui.activity.main.presenter.MarketPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/10/25 15:54:02
 */

public class MarketFragment extends BaseFragment implements MarketContract.View {

    @Inject
    MarketPresenter mPresenter;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_market, null);
       ButterKnife.bind(this, view);
       Bundle mBundle = getArguments();
       return view;
   }


    @Override
    protected void setupFragmentComponent() {
       DaggerMarketComponent
               .builder()
               .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
               .marketModule(new MarketModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(MarketContract.MarketContractPresenter presenter) {
        mPresenter = (MarketPresenter) presenter;
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