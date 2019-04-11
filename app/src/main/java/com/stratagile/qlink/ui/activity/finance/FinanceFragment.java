package com.stratagile.qlink.ui.activity.finance;

import android.content.Intent;
import android.os.Bundle;
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
import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.ui.activity.finance.component.DaggerFinanceComponent;
import com.stratagile.qlink.ui.activity.finance.contract.FinanceContract;
import com.stratagile.qlink.ui.activity.finance.module.FinanceModule;
import com.stratagile.qlink.ui.activity.finance.presenter.FinancePresenter;
import com.stratagile.qlink.ui.adapter.finance.ProduceListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/08 17:36:49
 */

public class FinanceFragment extends BaseFragment implements FinanceContract.View {

    @Inject
    FinancePresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    ProduceListAdapter produceListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                getProduct();
            }
        });
        produceListAdapter = new ProduceListAdapter(new ArrayList<>());
        recyclerView.setAdapter(produceListAdapter);
        produceListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), ProductDetailActivity.class).putExtra("productId", produceListAdapter.getData().get(position).getId()));
            }
        });
        initDataFromLocal();
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerFinanceComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .financeModule(new FinanceModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(FinanceContract.FinanceContractPresenter presenter) {
        mPresenter = (FinancePresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {
        getProduct();
    }

    private void getProduct() {
        Map map = new HashMap<String, String>();
        map.put("page", "1");
        map.put("size", "50");
        mPresenter.getProductList(map);
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
    public void getProductBack(Product product) {
        produceListAdapter.setNewData(product.getData());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}