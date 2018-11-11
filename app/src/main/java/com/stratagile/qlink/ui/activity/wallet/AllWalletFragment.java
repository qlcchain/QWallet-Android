package com.stratagile.qlink.ui.activity.wallet;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.CurrencyBean;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eth.EthWalletDetailActivity;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerAllWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletPresenter;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.wallet.TokensAdapter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/24 10:17:57
 */

public class AllWalletFragment extends BaseFragment implements AllWalletContract.View {

    @Inject
    AllWalletPresenter mPresenter;
    TokensAdapter tokensAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ivWalletAvatar)
    ImageView ivWalletAvatar;
    @BindView(R.id.tvWalletName)
    TextView tvWalletName;
    @BindView(R.id.tvWalletAddress)
    TextView tvWalletAddress;
    @BindView(R.id.tvWalletAsset)
    TextView tvWalletAsset;
    @BindView(R.id.tvWalletMoney)
    TextView tvWalletMoney;
    @BindView(R.id.tvWalletGas)
    TextView tvWalletGas;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private double walletAsset;

    private HashMap<AllWallet, Double> allWalletMoney = new HashMap<>();

    private AllWallet currentSelectWallet;

    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_wallet, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        tokensAdapter = new TokensAdapter(null);
        recyclerView.setAdapter(tokensAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10, getActivity())));
        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    refreshLayout.setEnabled(true);
                } else {
                    refreshLayout.setEnabled(false);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                initData();
            }
        });
        initData();
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
    public void getNeoWalletInfoBack(ArrayList<TokenInfo> tokenInfos) {
        getTokenPrice(tokenInfos);
    }

    private void getTokenPrice(ArrayList<TokenInfo> tokenInfos) {
        HashMap<String, Object> infoMap = new HashMap<>();
        String[] tokens = new String[tokenInfos.size()];
        for (int i = 0; i < tokenInfos.size(); i++) {
            tokens[i] = tokenInfos.get(i).getTokenSymol();
        }
        infoMap.put("symbols", tokens);
        infoMap.put("coin", ConstantValue.currencyBean.getName());
        mPresenter.getToeknPrice(tokenInfos, infoMap);
    }

    @Override
    public void getTokenPriceBack(ArrayList<TokenInfo> tokenInfos) {
        if (tokenInfos.size() == 0) {
            return;
        }
        walletAsset = 0;
        viewModel.walletTypeMutableLiveData.postValue(tokenInfos.get(0).getWalletType());
        ArrayList<String> symbols = new ArrayList<>();
        tokensAdapter.setNewData(tokenInfos);
        for (int i = 0; i < tokenInfos.size(); i++) {
            symbols.add(tokenInfos.get(i).getTokenSymol());
            walletAsset += tokenInfos.get(i).getTokenValue() * tokenInfos.get(i).getTokenPrice();
            if (tokenInfos.get(i).getWalletType() == AllWallet.WalletType.EthWallet) {
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eth_wallet));
            } else if (tokenInfos.get(i).getWalletType() == AllWallet.WalletType.NeoWallet) {
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_neo_wallet));
            } else if (tokenInfos.get(i).getWalletType() == AllWallet.WalletType.EosWallet) {
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eos_wallet));
            }
        }
        viewModel.tokens.postValue(symbols);
        BigDecimal b = new BigDecimal(new Double((walletAsset)).toString());
        walletAsset = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (walletAsset != 0) {
            tvWalletAsset.setText(ConstantValue.currencyBean.getCurrencyImg() + " " + walletAsset);
        } else {
            tvWalletAsset.setText("- -");
        }
        for (AllWallet allWallet : allWalletMoney.keySet()) {
            if (allWallet.getWalletAddress().toLowerCase().equals(tokenInfos.get(0).getWalletAddress().toLowerCase())) {
                allWalletMoney.put(allWallet, walletAsset);
                break;
            }
        }
        setAllWalletMoney();
    }

    private void setAllWalletMoney() {
        double allMoney = 0;
        for (AllWallet allWallet : allWalletMoney.keySet()) {
            allMoney += allWalletMoney.get(allWallet);
        }
        tvWalletMoney.setText(ConstantValue.currencyBean.getCurrencyImg() + " " + new BigDecimal(allMoney).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
    }


    @Override
    public void setPresenter(AllWalletContract.AllWalletContractPresenter presenter) {
        mPresenter = (AllWalletPresenter) presenter;
    }

    private void initData() {
        boolean hasSelectedWallet = false;
        tokensAdapter.setNewData(new ArrayList<>());
        tvWalletAsset.setText("- -");
        viewModel.qrcode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                KLog.i(s);
            }
        });
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        for (int i = 0; i < ethWallets.size(); i++) {
            if (ethWallets.get(i).getIsCurrent()) {
                hasSelectedWallet = true;
                getEthToken(ethWallets.get(i));
            }
        }
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (neoWallets.size() != 0) {
            for (int i = 0; i < neoWallets.size(); i++) {
                if (neoWallets.get(i).getIsCurrent()) {
                    hasSelectedWallet = true;
                    getNeoToken(neoWallets.get(i));
                }
            }
        }
        if (!hasSelectedWallet) {
            if (ethWallets.size() == 0 && neoWallets.size() == 0) {
                startActivityForResult(new Intent(getActivity(), SelectWalletTypeActivity.class), 2);
            }
            if (ethWallets.size() != 0) {
                ethWallets.get(0).setCurrent(true);
                AppConfig.getInstance().getDaoSession().getEthWalletDao().update(ethWallets.get(0));
                initData();
                return;
            } else if (neoWallets.size() != 0) {
                neoWallets.get(0).setIsCurrent(true);
                AppConfig.getInstance().getDaoSession().getWalletDao().update(neoWallets.get(0));
                initData();
                return;
            }
        }
        tokensAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), EthTransactionRecordActivity.class);
                intent.putExtra("tokenInfo", tokensAdapter.getData().get(position));
                startActivity(intent);
            }
        });

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

    private void getEthToken(EthWallet ethWallet) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", ethWallet.getAddress());
//        infoMap.put("token", ConstantValue.ethContractAddress);
        mPresenter.getETHWalletDetail(ethWallet.getAddress(), infoMap);
        tvWalletAddress.setText(ethWallet.getAddress());
        tvWalletName.setText(ethWallet.getName());

        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.EthWallet);
        allWallet.setEthWallet(ethWallet);
        allWallet.setWalletName(ethWallet.getName());
        allWallet.setWalletAddress(ethWallet.getAddress());
        currentSelectWallet = allWallet;
        allWalletMoney.put(allWallet, 0d);
    }

    private void getNeoToken(Wallet wallet) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", wallet.getAddress());
//        infoMap.put("token", ConstantValue.ethContractAddress);
        mPresenter.getNeoWalletDetail(wallet.getAddress(), infoMap);
        tvWalletAddress.setText(wallet.getAddress());
        if (wallet.getName() == null || wallet.getName().equals("")) {
            wallet.setName(wallet.getAddress());
            AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
            tvWalletName.setText(wallet.getAddress());
        } else {
            tvWalletName.setText(wallet.getName());
        }

        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.NeoWallet);
        allWallet.setWallet(wallet);
        allWallet.setWalletName(wallet.getName());
        allWallet.setWalletAddress(wallet.getAddress());
        currentSelectWallet = allWallet;
        allWalletMoney.put(allWallet, 0d);
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

    @OnClick({R.id.tvWalletName, R.id.tvWalletAddress, R.id.cardView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvWalletName:
                startActivityForResult(new Intent(getActivity(), ChooseWalletActivity.class), 0);
                getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                break;
            case R.id.tvWalletAddress:
                ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText("", tvWalletAddress.getText().toString()));
                ToastUtil.displayShortToast("copy success");
                break;
            case R.id.cardView:
                if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EthWallet) {
                    startActivityForResult(new Intent(getActivity(), EthWalletDetailActivity.class).putExtra("ethwallet", currentSelectWallet.getEthWallet()).putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal()), 1);
                } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
                    startActivityForResult(new Intent(getActivity(), EthWalletDetailActivity.class).putExtra("neowallet", currentSelectWallet.getWallet()).putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal()), 1);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            initData();
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            allWalletMoney.remove(currentSelectWallet);
            List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            if (neoWallets.size() != 0) {
                neoWallets.get(0).setIsCurrent(true);
                AppConfig.getInstance().getDaoSession().getWalletDao().update(neoWallets.get(0));
            } else {
                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                if (ethWallets.size() != 0) {
                    AppConfig.getInstance().getDaoSession().getEthWalletDao().update(ethWallets.get(0));
                }
            }
            initData();
        } else if (requestCode == 2) {
            initData();
        }
    }

}