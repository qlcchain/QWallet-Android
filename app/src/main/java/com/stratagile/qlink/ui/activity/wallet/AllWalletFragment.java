package com.stratagile.qlink.ui.activity.wallet;

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
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.CurrencyBean;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerAllWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletPresenter;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.wallet.AllWalletListAdapter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/24 10:17:57
 */

public class AllWalletFragment extends BaseFragment implements AllWalletContract.View {

    @Inject
    AllWalletPresenter mPresenter;
    AllWalletListAdapter allWalletListAdapter;
    ArrayList<AllWallet> allWallets = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_wallet, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        initData();
        recyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10, getActivity())));
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerAllWalletComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .allWalletModule(new AllWalletModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void setPresenter(AllWalletContract.AllWalletContractPresenter presenter) {
        mPresenter = (AllWalletPresenter) presenter;
    }

    private void initData() {
        allWallets.clear();
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        for (int i = 0; i < ethWallets.size(); i++) {
            AllWallet allWallet = new AllWallet();
            allWallet.setEthWallet(ethWallets.get(i));
            allWallet.setWalletType(AllWallet.WalletType.EthWallet);
            allWallet.setWalletName(ethWallets.get(i).getName());
            allWallets.add(allWallet);
        }
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (neoWallets.size() != 0) {
            for (int i = 0; i < neoWallets.size(); i++) {
                AllWallet allWallet = new AllWallet();
                allWallet.setWallet(neoWallets.get(i));
                allWallet.setWalletType(AllWallet.WalletType.NeoWallet);
                allWallet.setWalletName(neoWallets.get(i).getAddress());
                allWallets.add(allWallet);
            }
        }
        if (allWalletListAdapter == null) {
            allWalletListAdapter = new AllWalletListAdapter(allWallets);
            recyclerView.setAdapter(allWalletListAdapter);
            allWalletListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    startActivity(new Intent(getActivity(), AllWalletTokenActivity.class).putExtra("walletName", allWallets.get(position).getWalletName()));
                }
            });
        } else {
            allWalletListAdapter.setNewData(allWallets);
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        refreshLayout.setRefreshing(false);

        String currencyName = SpUtil.getString(getActivity(), ConstantValue.currencyUnit, "USD");
        if (currencyName.equals("USD")) {
            CurrencyBean currencyBean = new CurrencyBean("USD", true);
            currencyBean.setCurrencyImg("$");
            ConstantValue.currencyBean = currencyBean;
        }
        if (currencyName.equals("CNY")) {
            CurrencyBean currencyBean = new CurrencyBean("CNY", true);
            currencyBean.setCurrencyImg("¥");
            ConstantValue.currencyBean = currencyBean;
        }
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