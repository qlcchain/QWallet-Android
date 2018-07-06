package com.stratagile.qlink.ui.activity.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.adapter.wallet.WalletListAdapter;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerWalletListComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletListContract;
import com.stratagile.qlink.ui.activity.wallet.module.WalletListModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletListPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/09 15:08:22
 */

public class WalletListFragment extends MyBaseFragment implements WalletListContract.View ,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {

    @Inject
    WalletListPresenter mPresenter;
    @Inject
    WalletListAdapter walletListAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String mType;
    private int mNextPage;
    private static final int ONE_PAGE_SIZE = 5;
    private int total;
    private static final String ARG_TYPE = "arg_type";

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.normal_list_layout, null);
       ButterKnife.bind(this, view);
       Bundle mBundle = getArguments();
       refreshLayout.setOnRefreshListener(this);
       walletListAdapter.setOnLoadMoreListener(this, recyclerView);
       walletListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
           @Override
           public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

           }
        });
       return view;
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }
    @Override
    public void fetchData() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDataFromServer(true);
            }
        });
    }

    public void loadDataFromServer(boolean showRefresh) {
        refreshLayout.setRefreshing(showRefresh);
        if (showRefresh) {
            mNextPage = 1;
        }
        mPresenter.getWalletListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }

    public static WalletListFragment newInstance(String param) {
        WalletListFragment fragment = new WalletListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mNextPage = 1;
        loadDataFromServer(true);
    }

    @Override
    public void onLoadMoreRequested() {
        if (walletListAdapter.getData().size() < ONE_PAGE_SIZE || walletListAdapter.getData().size() >= total) {
            walletListAdapter.loadMoreEnd(false);
            return;
        }
        mNextPage ++;
        mPresenter.getWalletListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }


    @Override
    protected void setupFragmentComponent() {
       DaggerWalletListComponent
               .builder()
               .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
               .walletListModule(new WalletListModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(WalletListContract.WalletListContractPresenter presenter) {
        mPresenter = (WalletListPresenter) presenter;
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