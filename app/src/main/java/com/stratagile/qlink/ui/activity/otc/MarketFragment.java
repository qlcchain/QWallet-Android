package com.stratagile.qlink.ui.activity.otc;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.EntrustOrderList;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.otc.component.DaggerMarketComponent;
import com.stratagile.qlink.ui.activity.otc.contract.MarketContract;
import com.stratagile.qlink.ui.activity.otc.module.MarketModule;
import com.stratagile.qlink.ui.activity.otc.presenter.MarketPresenter;
import com.stratagile.qlink.ui.adapter.BottomMarginItemDecoration;
import com.stratagile.qlink.ui.adapter.otc.EntrustOrderListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private MainViewModel viewModel;
    private EntrustOrderListAdapter entrustOrderListAdapter;

    public static String currentOrderType = ConstantValue.orderTypeSell;
    public int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market1, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        currentOrderType = ConstantValue.orderTypeSell;
        entrustOrderListAdapter = new EntrustOrderListAdapter(new ArrayList<>());
        entrustOrderListAdapter.setEnableLoadMore(true);
        recyclerView.addItemDecoration(new BottomMarginItemDecoration((int) getActivity().getResources().getDimension(R.dimen.x20)));
        recyclerView.setAdapter(entrustOrderListAdapter);
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        viewModel.currentEntrustOrderType.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                currentPage = 0;
                currentOrderType = s;
                refreshLayout.setRefreshing(true);
                entrustOrderListAdapter.setNewData(new ArrayList<>());
                getOrderList();
            }
        });

        entrustOrderListAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getOrderList();
            }
        }, recyclerView);

//        viewModel.currentUserAccount.observe(this, new Observer<UserAccount>() {
//            @Override
//            public void onChanged(@Nullable UserAccount userAccount) {
//                currentPage = 0;
//                getOrderList();
//            }
//        });

        entrustOrderListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (entrustOrderListAdapter.getData().get(position).getType().equals(ConstantValue.orderTypeSell)) {
                    startActivity(new Intent(getActivity(), BuyQgasActivity.class).putExtra("order", entrustOrderListAdapter.getData().get(position)));
                } else {
                    startActivity(new Intent(getActivity(), SellQgasActivity.class).putExtra("order", entrustOrderListAdapter.getData().get(position)));
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                entrustOrderListAdapter.setNewData(new ArrayList<>());
                getOrderList();
            }
        });
    }

    public void getOrderList() {
        currentPage++;
        refreshLayout.setRefreshing(false);
        Map map = new HashMap<String, String>();
        map.put("userId", "");
        map.put("type", currentOrderType);
        map.put("page", currentPage + "");
        map.put("size", "5");
        mPresenter.getOrderList(map);
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
    public void setEntrustOrderList(ArrayList<EntrustOrderList.OrderListBean> list) {
        entrustOrderListAdapter.addData(list);
        if (currentPage != 1) {
            entrustOrderListAdapter.loadMoreComplete();
        }
        if (list.size() == 0) {
            entrustOrderListAdapter.loadMoreEnd(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}