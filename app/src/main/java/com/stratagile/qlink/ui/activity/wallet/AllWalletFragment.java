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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.NeoNodeRPC;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.ClaimData;
import com.stratagile.qlink.entity.NeoTransfer;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.WinqGasBack;
import com.stratagile.qlink.entity.eos.EosQrBean;
import com.stratagile.qlink.entity.eventbus.ChangeCurrency;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.ui.activity.eos.EosActivationActivity;
import com.stratagile.qlink.ui.activity.eos.EosResourceManagementActivity;
import com.stratagile.qlink.ui.activity.eos.EosTransferActivity;
import com.stratagile.qlink.ui.activity.eth.EthWalletDetailActivity;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerAllWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletPresenter;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.wallet.TokensAdapter;
import com.stratagile.qlink.utils.EosUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import neoutils.Neoutils;
import sun.misc.BASE64Decoder;

import static java.math.BigDecimal.ROUND_HALF_UP;

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
    @BindView(R.id.ivClaim)
    ImageView ivClaim;
    @BindView(R.id.tvGasValue)
    TextView tvGasValue;
    @BindView(R.id.tvClaim)
    TextView tvClaim;
    @BindView(R.id.llGetGas)
    LinearLayout llGetGas;
    @BindView(R.id.llResouces)
    LinearLayout llResouces;

    private double walletAsset;

    private HashMap<AllWallet, Double> allWalletMoney = new HashMap<>();

    private AllWallet currentSelectWallet;

    private MainViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_wallet, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        Bundle mBundle = getArguments();
        tokensAdapter = new TokensAdapter(null);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
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
        viewModel.qrcode.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                KLog.i(s);
                if (ETHWalletUtils.isETHValidAddress(s)) {
                    if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EthWallet) {
                        if (!currentSelectWallet.getEthWallet().getIsLook()) {

                        } else {
                            ToastUtil.displayShortToast("Olny Watch ETH Wallet Cannot Transfer");
                            return;
                        }
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), EthTransferActivity.class).putExtra("walletAddress", s).putExtra("selfAddress", currentSelectWallet.getWalletAddress());
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast("Please scan the ETH wallet address and make a transfer.");
                    }
                } else if (Neoutils.validateNEOAddress(s)) {
                    if (currentSelectWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), NeoTransferActivity.class).putExtra("walletAddress", s);
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast("Please scan the NEO wallet address and make a transfer.");
                    }
                } else if (EosUtil.isEosName(s)){
                    if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EosWallet) {
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), EosTransferActivity.class).putExtra("walletAddress", s);
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast("Please scan the EOS wallet address and make a transfer.");
                    }
                } else {
                    try {
                        EosQrBean eosQrBean = new Gson().fromJson(s, EosQrBean.class);
                        if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EosWallet) {
                            startActivity(new Intent(getActivity(), EosActivationActivity.class).putExtra("eosQrBean", eosQrBean).putExtra("eoswallet", currentSelectWallet.getEosAccount()));
                        } else {
                            startActivity(new Intent(getActivity(), EosActivationActivity.class).putExtra("eosQrBean", eosQrBean));
                        }
                    } catch (Exception e) {
                        ToastUtil.displayShortToast("Invalid QR code");
                    }
                }
            }
        });
        viewModel.timeStampAllWalletInitData.observe(this, new Observer<Long>() {
            @Override
            public void onChanged(@Nullable Long aLong) {
                initData();
            }
        });
        tokensAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), EthTransactionRecordActivity.class);
                intent.putExtra("tokenInfo", tokensAdapter.getData().get(position));
                ArrayList<TokenInfo> arrayList = new ArrayList<>();
                arrayList.addAll(tokensAdapter.getData());
                intent.putParcelableArrayListExtra("tokens", arrayList);
                startActivity(intent);
            }
        });
        View view1 = getLayoutInflater().inflate(R.layout.item_empty_layout, null, false);
        tokensAdapter.setEmptyView(view1);
        initData();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getbalance(ChangeWallet changeWallet) {
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCurrency(ChangeCurrency changeWallet) {
        initData();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
        viewModel.tokenInfoMutableLiveData.postValue(tokenInfos);
        tokensAdapter.setNewData(tokenInfos);
        for (int i = 0; i < tokenInfos.size(); i++) {
            symbols.add(tokenInfos.get(i).getTokenSymol());
            String value = tokenInfos.get(i).getTokenValue() / (Math.pow(10.0, tokenInfos.get(i).getTokenDecimals())) + "";
            walletAsset += Double.parseDouble(value) * tokenInfos.get(i).getTokenPrice();
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
        walletAsset = b.setScale(2, ROUND_HALF_UP).doubleValue();
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

    @Override
    public void getWinqGasBack(Balance balance) {
        tvWalletGas.setText(BigDecimal.valueOf(balance.getData().getQLC()).setScale(8, ROUND_HALF_UP).toPlainString() + "");
        viewModel.balanceMutableLiveData.postValue(balance);
    }

    @Override
    public void queryWinqGasBack(WinqGasBack winqGasBack) {
        showClaimDialog(winqGasBack.getData().getWinqGas());
    }

    @Override
    public void gotWinqGasSuccess(String s) {
        ToastUtil.displayShortToast(s);
    }

    private ClaimData claimData;

    @Override
    public void queryGasClaimBack(ClaimData claimData) {
        this.claimData = claimData;
        if (claimData.getData().getClaims().size() == 0) {
            llGetGas.setVisibility(View.GONE);
        } else {
            llGetGas.setVisibility(View.VISIBLE);
            tvGasValue.setText(setGasValue(claimData.getData()));
        }
    }

    private String setGasValue(ClaimData.DataBean dataBean) {
        BigDecimal bigDecimal = new BigDecimal(0.00000007);
        int value = 0;
        for (ClaimData.DataBean.ClaimsBean claimsBean : dataBean.getClaims()) {
//            bigDecimal.add(BigDecimal.valueOf(new Double(claimsBean.getClaim())));
            value += claimsBean.getValue() * (Integer.parseInt(claimsBean.getEnd()) - Integer.parseInt(claimsBean.getStart()));
        }
        bigDecimal = bigDecimal.multiply(BigDecimal.valueOf(value)).setScale(8, ROUND_HALF_UP);
        KLog.i(bigDecimal.toPlainString());
        return bigDecimal.toPlainString();
    }

    @Override
    public void claimGasBack(NeoTransfer baseBack) {
        closeProgressDialog();
        if (baseBack.getData().isTransferResult()) {
            ToastUtil.displayShortToast(getResources().getString(R.string.success));
        } else {

        }
        queryGas(Account.INSTANCE.getWallet().getAddress());
    }

    private void setAllWalletMoney() {
        double allMoney = 0;
        for (AllWallet allWallet : allWalletMoney.keySet()) {
            allMoney += allWalletMoney.get(allWallet);
        }
        tvWalletMoney.setText(ConstantValue.currencyBean.getCurrencyImg() + " " + BigDecimal.valueOf(allMoney).setScale(2, ROUND_HALF_UP).toPlainString());
    }


    @Override
    public void setPresenter(AllWalletContract.AllWalletContractPresenter presenter) {
        mPresenter = (AllWalletPresenter) presenter;
    }

    boolean hasSelectedWallet = false;

    private void initData() {
        hasSelectedWallet = false;
        tokensAdapter.setNewData(new ArrayList<>());
        tvWalletAsset.setText("- -");
        tvWalletGas.setText("- -");
        llGetGas.setVisibility(View.GONE);
        llResouces.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                            mPresenter.getWinqGas(neoWallets.get(i).getAddress());
                        }
                    }
                }
                List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
                if (eosAccounts.size() != 0) {
                    for (int i = 0; i < eosAccounts.size(); i++) {
                        if (eosAccounts.get(i).getIsCurrent()) {
                            hasSelectedWallet = true;
                            getEosToken(eosAccounts.get(i));
                        }
                    }
                }
                if (!hasSelectedWallet) {
                    if (ethWallets.size() == 0 && neoWallets.size() == 0 && eosAccounts.size() == 0 && eosAccounts.size() == 0) {
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
                    } else if (eosAccounts.size() != 0) {
                        eosAccounts.get(0).setIsCurrent(true);
                        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(eosAccounts.get(0));
                        initData();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void initDataFromLocal() {

    }

    private void getEthToken(EthWallet ethWallet) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", ethWallet.getAddress());
        mPresenter.getETHWalletDetail(ethWallet.getAddress(), infoMap);
        tvWalletAddress.post(new Runnable() {
            @Override
            public void run() {
                tvWalletAddress.setText(ethWallet.getAddress());
                tvWalletName.setText(ethWallet.getName());
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eth_wallet));
                llGetGas.setVisibility(View.GONE);
            }
        });
        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.EthWallet);
        allWallet.setEthWallet(ethWallet);
        allWallet.setWalletName(ethWallet.getName());
        allWallet.setWalletAddress(ethWallet.getAddress());
        currentSelectWallet = allWallet;
        viewModel.allWalletMutableLiveData.postValue(currentSelectWallet);
        viewModel.walletTypeMutableLiveData.postValue(AllWallet.WalletType.EthWallet);
        allWalletMoney.put(allWallet, 0d);
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (neoWallets.size() != 0) {
            Account.INSTANCE.fromWIF(neoWallets.get(0).getWif());
            mPresenter.getWinqGas(neoWallets.get(0).getAddress());
        }
    }

    private void getEosToken(EosAccount eosAccount) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("account", eosAccount.getAccountName());
        mPresenter.getEosWalletDetail(eosAccount.getAccountName(), infoMap);
        tvWalletAddress.post(new Runnable() {
            @Override
            public void run() {
                tvWalletAddress.setText(eosAccount.getAccountName());
                llResouces.setVisibility(View.VISIBLE);
                tvWalletName.setText(eosAccount.getWalletName() == null ? eosAccount.getAccountName() : eosAccount.getWalletName());
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eos_wallet));
                llGetGas.setVisibility(View.GONE);
            }
        });
        viewModel.walletTypeMutableLiveData.postValue(AllWallet.WalletType.EosWallet);
        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.EosWallet);
        allWallet.setEosAccount(eosAccount);
        allWallet.setWalletName(eosAccount.getAccountName());
        allWallet.setWalletAddress(eosAccount.getAccountName());
        currentSelectWallet = allWallet;
        viewModel.allWalletMutableLiveData.postValue(currentSelectWallet);
        allWalletMoney.put(allWallet, 0d);
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (neoWallets.size() != 0) {
            Account.INSTANCE.fromWIF(neoWallets.get(0).getWif());
            mPresenter.getWinqGas(neoWallets.get(0).getAddress());
        }
    }

    private void getNeoToken(Wallet wallet) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", wallet.getAddress());
        tvWalletName.post(new Runnable() {
            @Override
            public void run() {
                tvGasValue.setText("- -");
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_neo_wallet));
                tvWalletAddress.setText(wallet.getAddress());
                if (wallet.getName() == null || wallet.getName().equals("")) {
                    wallet.setName(wallet.getAddress());
                    AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                    tvWalletName.setText(wallet.getAddress());
                } else {
                    tvWalletName.setText(wallet.getName());
                }

            }
        });
        viewModel.walletTypeMutableLiveData.postValue(AllWallet.WalletType.NeoWallet);
        mPresenter.getNeoWalletDetail(wallet.getAddress(), infoMap);
        queryGas(wallet.getAddress());
        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.NeoWallet);
        allWallet.setWallet(wallet);
        allWallet.setWalletName(wallet.getName());
        allWallet.setWalletAddress(wallet.getAddress());
        currentSelectWallet = allWallet;
        viewModel.allWalletMutableLiveData.postValue(currentSelectWallet);
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

    @OnClick({R.id.tvWalletName, R.id.tvWalletAddress, R.id.cardView, R.id.ivClaim, R.id.tvClaim, R.id.tvWalletGas, R.id.llResouces})
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
                ToastUtil.displayShortToast(getResources().getString(R.string.copy_success));
                break;
            case R.id.cardView:
                if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EthWallet) {
                    startActivityForResult(new Intent(getActivity(), EthWalletDetailActivity.class).putExtra("ethwallet", currentSelectWallet.getEthWallet()).putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal()), 1);
                } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
                    startActivityForResult(new Intent(getActivity(), EthWalletDetailActivity.class).putExtra("neowallet", currentSelectWallet.getWallet()).putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal()), 1);
                } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EosWallet) {
                    startActivityForResult(new Intent(getActivity(), EthWalletDetailActivity.class).putExtra("eoswallet", currentSelectWallet.getEosAccount()).putExtra("walletType", AllWallet.WalletType.EosWallet.ordinal()), 1);
                }
                break;
            case R.id.ivClaim:
                queryWinqGas();
                break;
            case R.id.tvClaim:
                getGas();
                break;
            case R.id.tvWalletGas:
                queryWinqGas();
                break;
            case R.id.llResouces:
                startActivity(new Intent(getActivity(), EosResourceManagementActivity.class).putExtra("eosAccount", currentSelectWallet.getEosAccount()));
                break;
            default:
                break;
        }
    }

    private void queryWinqGas() {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("p2pId", SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
        mPresenter.queryWinqGas(infoMap);
    }

    private void showClaimDialog(double winqGas) {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        tvContent.setText("There are " + winqGas + " available to claim.");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity());
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvOk = view.findViewById(R.id.tvOpreate);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winqGas == 0.00000000) {
                    sweetAlertDialog.cancel();
                } else {
                    sweetAlertDialog.cancel();
                    gotWinqGas();

                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
            }
        });
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
    }

    private void gotWinqGas() {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("p2pId", SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
        if (Account.INSTANCE.getWallet().getAddress() != null) {
            infoMap.put("address", Account.INSTANCE.getWallet().getAddress());
            mPresenter.gotWinqGas(infoMap);
        } else {
            ToastUtil.displayShortToast("please create a neo wallet first");
        }
    }

    private void queryGas(String address) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("address", address);
        mPresenter.getNeoGasClaim(infoMap);
    }

    private void getGas() {
        showProgressDialog();
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
        String txid = neoNodeRPC.claimGAS(Account.INSTANCE.getWallet(), claimData, tvGasValue.getText().toString());
        KLog.i(txid);
        mPresenter.claimGas(Account.INSTANCE.getWallet().getAddress(), tvGasValue.getText().toString(), txid);
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