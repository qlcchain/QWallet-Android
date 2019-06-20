package com.stratagile.qlink.ui.activity.otc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.otc.component.DaggerMarketComponent;
import com.stratagile.qlink.ui.activity.otc.contract.MarketContract;
import com.stratagile.qlink.ui.activity.otc.module.MarketModule;
import com.stratagile.qlink.ui.activity.otc.presenter.MarketPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.market
 * @Description: $description
 * @date 2019/06/14 16:23:19
 */

public class MarketFragment extends BaseFragment implements MarketContract.View {

    @Inject
    MarketPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market1, null);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}