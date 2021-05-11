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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlc.QLCAPI;
import com.stratagile.qlc.entity.QlcTokenbalance;
import com.stratagile.qlc.utils.QlcUtil;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.ClaimData;
import com.stratagile.qlink.entity.NeoTransfer;
import com.stratagile.qlink.entity.QrEntity;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.WinqGasBack;
import com.stratagile.qlink.entity.eos.EosQrBean;
import com.stratagile.qlink.entity.eventbus.ChangeCurrency;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.ui.activity.defi.EthTransformActivity;
import com.stratagile.qlink.ui.activity.defi.TransformQlcActivity;
import com.stratagile.qlink.ui.activity.defi.WalletConnectActivity;
import com.stratagile.qlink.ui.activity.eos.EosActivationActivity;
import com.stratagile.qlink.ui.activity.eos.EosResourceManagementActivity;
import com.stratagile.qlink.ui.activity.eos.EosTransferActivity;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicShowActivity;
import com.stratagile.qlink.ui.activity.eth.EthTransferActivity;
import com.stratagile.qlink.ui.activity.eth.WalletDetailActivity;
import com.stratagile.qlink.ui.activity.main.MainViewModel;
import com.stratagile.qlink.ui.activity.neo.NeoTransferActivity;
import com.stratagile.qlink.ui.activity.neo.NeoWalletInfoActivity;
import com.stratagile.qlink.ui.activity.qlc.QlcMnemonicShowActivity;
import com.stratagile.qlink.ui.activity.qlc.QlcTransferActivity;
import com.stratagile.qlink.ui.activity.stake.MyStakeActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerAllWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletPresenter;
import com.stratagile.qlink.ui.adapter.wallet.TokensAdapter;
import com.stratagile.qlink.utils.EosUtil;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.FireBaseUtils;
import com.stratagile.qlink.utils.NeoUtils;
import com.stratagile.qlink.utils.Network;
import com.stratagile.qlink.utils.QlcReceiveUtilsKt;
import com.stratagile.qlink.utils.ReceiveBack;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.SweetAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qlc.bean.Pending;
import qlc.mng.AccountMng;
import qlc.mng.LedgerMng;
import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.AccountRpc;
import qlc.rpc.impl.LedgerRpc;
import qlc.utils.Helper;
import wendu.dsbridge.DWebView;
import wendu.dsbridge.OnReturnValue;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/24 10:17:57
 */

public class AllWalletFragment extends BaseFragment implements AllWalletContract.View {
    @BindView(R.id.llNeoQlcTransfer)
    CardView llNeoQlcTransfer;
    @BindView(R.id.llEthQlcTransfer)
    CardView llEthQlcTransfer;

    @Override
    protected void initDataFromNet() {

    }

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
    //    @BindView(R.id.tvWalletMoney)
//    TextView tvWalletMoney;
//    @BindView(R.id.tvWalletGas)
//    TextView tvWalletGas;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    //    @BindView(R.id.ivClaim)
//    ImageView ivClaim;
    @BindView(R.id.tvGasValue)
    TextView tvGasValue;
    @BindView(R.id.tvClaim)
    TextView tvClaim;
    @BindView(R.id.llGetGas)
    LinearLayout llGetGas;
    @BindView(R.id.llResouces)
    LinearLayout llResouces;
    @BindView(R.id.llStake)
    CardView llStake;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.ivTokenAvatar)
    ImageView ivTokenAvatar;
    @BindView(R.id.tvGasIntroduce)
    TextView tvGasIntroduce;
    @BindView(R.id.tvBackUpNow)
    TextView tvBackUpNow;
    @BindView(R.id.llBackUp)
    LinearLayout llBackUp;
    @BindView(R.id.main_content)
    RelativeLayout mainContent;
    @BindView(R.id.tvPending)
    TextView tvPending;
    @BindView(R.id.status_bar)
    TextView statusBar;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.rlWallet)
    RelativeLayout rlWallet;
    @BindView(R.id.rl1)
    RelativeLayout rl1;
//    @BindView(R.id.cardView)
//    CardView cardView;
//    @BindView(R.id.tvGasValue)
//    TextView tvGasValue;

    private double walletAsset;

    private boolean isPending;
    private HashMap<AllWallet, Double> allWalletMoney = new HashMap<>();

    private AllWallet currentSelectWallet;

    private MainViewModel viewModel;

    private Wallet selectedNeoWallet;
    private EthWallet selectedEthWallet;
    private QLCAccount selectedQlcWallet;

    private final int START_ACTIVITY_BACKUP = 2;

    private Pending pending;

    private String errorTxid = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_wallet, null);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        Bundle mBundle = getArguments();
        tokensAdapter = new TokensAdapter(null);
        errorTxid = FileUtil.getJson(getActivity(), "airdrop.json");
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
        recyclerView.setAdapter(tokensAdapter);

        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(getActivity()), UIUtils.getStatusBarHeight(getActivity()));
        statusBar.setLayoutParams(llp);

        isPending = false;
//        recyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10, getActivity())));
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
//                autoGenerateWallet();
                isPending = false;
                tvPending.setText("");
                pending = null;
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
                            ToastUtil.displayShortToast(getString(R.string.only_watch_eth_wallet_cannot_transfer));
                            return;
                        }
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), EthTransferActivity.class).putExtra("walletAddress", s).putExtra("selfAddress", currentSelectWallet.getWalletAddress());
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.please_scan_the_eth_wallet_address_and_make_a_transfer));
                    }
                } else if (NeoUtils.isValidAddress(s)) {
                    if (currentSelectWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), NeoTransferActivity.class).putExtra("walletAddress", s);
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.please_scan_the_neo_wallet_address_and_make_a_transfer));
                    }
                } else if (EosUtil.isEosName(s)) {
                    if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EosWallet) {
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), EosTransferActivity.class).putExtra("walletAddress", s);
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.please_scan_the_eos_wallet_address_and_make_a_transfer));
                    }
                } else if (AccountMng.isValidAddress(s)) {
                    if (currentSelectWallet.getWalletType() == AllWallet.WalletType.QlcWallet) {
                        ArrayList<TokenInfo> arrayList = new ArrayList<>();
                        arrayList.addAll(tokensAdapter.getData());
                        Intent intent = new Intent(getActivity(), QlcTransferActivity.class).putExtra("walletAddress", s);
                        intent.putParcelableArrayListExtra("tokens", arrayList);
                        startActivity(intent);
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.please_scan_the_qlc_wallet_address_and_make_a_transfer));
                    }
                } else if (s.startsWith("wc:")) {
                    startActivity(new Intent(getActivity(), WalletConnectActivity.class).putExtra("session", s));
                }else {
                    try {
                        EosQrBean eosQrBean = new Gson().fromJson(s, EosQrBean.class);
                        if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EosWallet) {
                            startActivity(new Intent(getActivity(), EosActivationActivity.class).putExtra("eosQrBean", eosQrBean).putExtra("eoswallet", currentSelectWallet.getEosAccount()));
                        } else {
                            startActivity(new Intent(getActivity(), EosActivationActivity.class).putExtra("eosQrBean", eosQrBean));
                        }
                    } catch (Exception e) {
                        ToastUtil.displayShortToast(getString(R.string.invalid_qr_code));
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
        tvWalletAsset.post(new Runnable() {
            @Override
            public void run() {
                KLog.i(" 刷新adapter");
                if (tokenInfos.size() == 0) {
                    tokensAdapter.setNewData(null);
                    tokensAdapter.getEmptyView().setVisibility(View.GONE);
                    return;
                }
                tokensAdapter.getEmptyView().setVisibility(View.VISIBLE);
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
                        if (!selectedEthWallet.getIsBackup()) {
                            if (selectedEthWallet.getMnemonic() != null && !"".equals(selectedEthWallet.getMnemonic())) {
                                llBackUp.setVisibility(View.VISIBLE);
                                tvBackUpNow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        KLog.i("点击");
                                        startActivityForResult(new Intent(getActivity(), EthMnemonicShowActivity.class).putExtra("wallet", selectedEthWallet), START_ACTIVITY_BACKUP);
                                    }
                                });
                            }
                        }
                    } else if (tokenInfos.get(i).getWalletType() == AllWallet.WalletType.NeoWallet) {
                        if (!selectedNeoWallet.getIsBackup()) {
                            llBackUp.setVisibility(View.VISIBLE);
                            tvBackUpNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    KLog.i("点击");
                                    startActivityForResult(new Intent(getActivity(), NeoWalletInfoActivity.class).putExtra("wallet", selectedNeoWallet), START_ACTIVITY_BACKUP);
                                }
                            });
                        }
                        ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_neo_wallet));
                    } else if (tokenInfos.get(i).getWalletType() == AllWallet.WalletType.EosWallet) {
                        ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eos_wallet));
                    } else if (tokenInfos.get(i).getWalletType() == AllWallet.WalletType.QlcWallet) {
                        if (!selectedQlcWallet.getIsBackUp()) {
                            llBackUp.setVisibility(View.VISIBLE);
                            tvBackUpNow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    KLog.i("点击");
                                    startActivityForResult(new Intent(getActivity(), QlcMnemonicShowActivity.class).putExtra("wallet", selectedQlcWallet), START_ACTIVITY_BACKUP);
                                }
                            });
                        }
                        ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_qlc_wallet));
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
            }
        });
    }

    @Override
    public void getWinqGasBack(Balance balance) {
//        tvWalletGas.setText(BigDecimal.valueOf(balance.getData().getQLC()).setScale(8, ROUND_HALF_UP).toPlainString() + "");
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
        if (claimData.getData().getUnclaimed() == 0) {
            llGetGas.setVisibility(View.GONE);
        } else {
            llGetGas.setVisibility(View.VISIBLE);
            tvGasValue.setText(new BigDecimal(claimData.getData().getUnclaimed() + "").toPlainString());
        }
    }

    private String setGasValue(ClaimData.DataBean dataBean) {
        BigDecimal bigDecimal = new BigDecimal(0.00000000);
//        int value = 0;
        for (ClaimData.DataBean.ClaimsBean claimsBean : dataBean.getClaims()) {
            bigDecimal = bigDecimal.add(BigDecimal.valueOf(Double.valueOf(claimsBean.getClaim())));
//            value += claimsBean.getValue() * (Integer.parseInt(claimsBean.getEnd()) - Integer.parseInt(claimsBean.getStart()));
        }
        bigDecimal = bigDecimal.setScale(8, ROUND_HALF_UP);
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
//        tvWalletMoney.setText(ConstantValue.currencyBean.getCurrencyImg() + " " + BigDecimal.valueOf(allMoney).setScale(2, ROUND_HALF_UP).toPlainString());
    }


    @Override
    public void setPresenter(AllWalletContract.AllWalletContractPresenter presenter) {
        mPresenter = (AllWalletPresenter) presenter;
    }

    boolean hasSelectedWallet = false;

    private void initData() {
//        testGenerateWallet();
        hasSelectedWallet = false;
        llBackUp.setVisibility(View.GONE);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tokensAdapter.setNewData(new ArrayList<>());
                tvWalletAsset.setText("- -");
                llGetGas.setVisibility(View.GONE);
                llResouces.setVisibility(View.GONE);
                llStake.setVisibility(View.GONE);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                for (int i = 0; i < ethWallets.size(); i++) {
                    if (ethWallets.get(i).getIsCurrent()) {
                        hasSelectedWallet = true;
                        selectedEthWallet = ethWallets.get(i);
                        getEthToken(ethWallets.get(i));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                llNeoQlcTransfer.setVisibility(View.GONE);
////                                llEthQlcTransfer.setVisibility(View.VISIBLE);
                                llStake.setVisibility(View.GONE);
                                llEthQlcTransfer.setVisibility(View.GONE);
                                tvPending.setText("");
                            }
                        });
                    }
                }
                List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                if (neoWallets.size() != 0) {
                    for (int i = 0; i < neoWallets.size(); i++) {
                        if (neoWallets.get(i).getIsCurrent()) {
                            hasSelectedWallet = true;
                            selectedNeoWallet = neoWallets.get(i);
                            KLog.i("创建钱包的结果：" + Account.INSTANCE.fromWIF(selectedNeoWallet.getWif()));
                            KLog.i("创建钱包的地址为：" + Account.INSTANCE.getWallet().getAddress());
                            getNeoToken(neoWallets.get(i));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
////                                    llNeoQlcTransfer.setVisibility(View.VISIBLE);
//                                    llEthQlcTransfer.setVisibility(View.GONE);
                                    llStake.setVisibility(View.GONE);
                                    llNeoQlcTransfer.setVisibility(View.GONE);
                                    tvPending.setText("");
                                }
                            });
                        }
                    }
                }
                List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
                if (eosAccounts.size() != 0) {
                    for (int i = 0; i < eosAccounts.size(); i++) {
                        if (eosAccounts.get(i).getIsCurrent()) {
                            hasSelectedWallet = true;
                            getEosToken(eosAccounts.get(i));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    llNeoQlcTransfer.setVisibility(View.GONE);
//                                    llEthQlcTransfer.setVisibility(View.GONE);

                                    llStake.setVisibility(View.GONE);
                                    llNeoQlcTransfer.setVisibility(View.GONE);
                                    tvPending.setText("");
                                }
                            });
                        }
                    }
                }
                List<QLCAccount> qlcAccounts = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
                for (QLCAccount wallet : qlcAccounts) {
                    if (wallet.getIsCurrent()) {
                        hasSelectedWallet = true;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                llNeoQlcTransfer.setVisibility(View.GONE);
//                                llEthQlcTransfer.setVisibility(View.GONE);
                                llStake.setVisibility(View.VISIBLE);
                                llNeoQlcTransfer.setVisibility(View.GONE);
                                llEthQlcTransfer.setVisibility(View.GONE);
                            }
                        });
                        selectedQlcWallet = wallet;
                        getQlcToken(wallet);
                    }
                }
                if (!hasSelectedWallet) {
                    if (ethWallets.size() == 0 && neoWallets.size() == 0 && eosAccounts.size() == 0 && qlcAccounts.size() == 0) {
                        autoGenerateWallet();
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
                    } else if (qlcAccounts.size() != 0) {
                        qlcAccounts.get(0).setIsCurrent(true);
                        AppConfig.getInstance().getDaoSession().getQLCAccountDao().update(qlcAccounts.get(0));
                        initData();
                    }
                }
            }
        }).start();
    }


    private void testGenerateWallet() {
        KLog.i("测试自动生成qlc钱包");
        String seed = QlcUtil.generateSeed().toLowerCase();
        try {
            JSONObject jsonObject = AccountMng.keyPairFromSeed(Helper.hexStringToBytes(seed), 0);
            String priKey = jsonObject.getString("privKey");
            String pubKey = jsonObject.getString("pubKey");
            KLog.i(jsonObject.toJSONString());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(seed);
            String mnemonics = AccountRpc.seedToMnemonics(jsonArray);
            KLog.i(mnemonics);
            String address = QlcUtil.publicToAddress(pubKey).toLowerCase();
            QLCAccount qlcAccount = new QLCAccount();
            qlcAccount.setPrivKey(priKey.toLowerCase());
            qlcAccount.setPubKey(pubKey);
            qlcAccount.setAddress(address);
            qlcAccount.setMnemonic(mnemonics);
            qlcAccount.setIsCurrent(true);
            qlcAccount.setAccountName(QLCAPI.Companion.getQlcWalletName());
            qlcAccount.setSeed(seed);
            qlcAccount.setIsAccountSeed(true);
            qlcAccount.setWalletIndex(0);
            KLog.i(qlcAccount.toString());
        } catch (QlcException e) {
            closeProgressDialog();
            e.printStackTrace();
        }
    }

    /**
     * 自动生成钱包逻辑
     */
    private void autoGenerateWallet() {
        String seed = QlcUtil.generateSeed().toLowerCase();
        try {
            JSONObject jsonObject = AccountMng.keyPairFromSeed(Helper.hexStringToBytes(seed), 0);
            String priKey = jsonObject.getString("privKey");
            String pubKey = jsonObject.getString("pubKey");
            KLog.i(jsonObject.toJSONString());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(seed);
            String mnemonics = AccountRpc.seedToMnemonics(jsonArray);
            KLog.i(mnemonics);
            String address = QlcUtil.publicToAddress(pubKey).toLowerCase();
            QLCAccount qlcAccount = new QLCAccount();
            qlcAccount.setPrivKey(priKey.toLowerCase());
            qlcAccount.setPubKey(pubKey);
            qlcAccount.setAddress(address);
            qlcAccount.setMnemonic(mnemonics);
            qlcAccount.setIsCurrent(true);
            qlcAccount.setAccountName(QLCAPI.Companion.getQlcWalletName());
            qlcAccount.setSeed(seed);
            qlcAccount.setIsAccountSeed(true);
            qlcAccount.setWalletIndex(0);
            AppConfig.instance.getDaoSession().getQLCAccountDao().insert(qlcAccount);

            try {
                EthWallet ethWallet = ETHWalletUtils.importMnemonic(ETHWalletUtils.ETH_JAXX_TYPE, Arrays.asList(mnemonics.split(" ")));
                ethWallet.setIsCurrent(false);
                KLog.i(ethWallet.toString());
                AppConfig.getInstance().getDaoSession().getEthWalletDao().insert(ethWallet);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (QlcException e) {
            closeProgressDialog();
            e.printStackTrace();
        }
//        io.neow3j.wallet.Account account = io.neow3j.wallet.Account.createAccount();
//        Wallet walletWinq = new Wallet();
//        walletWinq.setAddress(account.getAddress());
//        walletWinq.setWif(account.getECKeyPair().exportAsWIF());
//        walletWinq.setPrivateKey(Numeric.toHexStringNoPrefix(account.getECKeyPair().getPrivateKey()).toLowerCase());
//        walletWinq.setPublicKey(Numeric.toHexStringNoPrefix(account.getECKeyPair().getPublicKey()).toLowerCase());
//        walletWinq.setScriptHash(account.getScriptHash().toString());
//        walletWinq.setIsCurrent(false);
//        walletWinq.setName("NEO-Wallet 01");
//        AppConfig.getInstance().getDaoSession().getWalletDao().insert(walletWinq);
        Account.INSTANCE.createNewWallet();
        neoutils.Wallet wallet = Account.INSTANCE.getWallet();
        Wallet walletWinq = new Wallet();
        walletWinq.setAddress(wallet.getAddress());
        walletWinq.setWif(wallet.getWIF());
        walletWinq.setPrivateKey(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()).toLowerCase());
        walletWinq.setPublicKey(Account.INSTANCE.byteArray2String(wallet.getPublicKey()).toLowerCase());
        walletWinq.setScriptHash(Account.INSTANCE.byteArray2String(wallet.getHashedSignature()));
        walletWinq.setIsCurrent(false);
        walletWinq.setName("NEO-Wallet 01");
        AppConfig.getInstance().getDaoSession().getWalletDao().insert(walletWinq);
        initData();
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

    private void getQlcToken(QLCAccount qlcAccount) {
        tvWalletAddress.post(new Runnable() {
            @Override
            public void run() {
                tvWalletAddress.setText(qlcAccount.getAddress());
                tvWalletName.setText(qlcAccount.getAccountName());
                ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_qlc_wallet));
                llGetGas.setVisibility(View.GONE);
                llStake.setVisibility(View.VISIBLE);
            }
        });
        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.QlcWallet);
        allWallet.setQlcAccount(qlcAccount);
        allWallet.setWalletName(qlcAccount.getAccountName());
        allWallet.setWalletAddress(qlcAccount.getAddress());
        currentSelectWallet = allWallet;
        viewModel.allWalletMutableLiveData.postValue(currentSelectWallet);
        viewModel.walletTypeMutableLiveData.postValue(AllWallet.WalletType.QlcWallet);
        allWalletMoney.put(allWallet, 0d);

        new QLCAPI().walletGetBalance(qlcAccount.getAddress(), "", new QLCAPI.BalanceInter() {
            @Override
            public void onBack(@org.jetbrains.annotations.Nullable ArrayList<QlcTokenbalance> baseResult, @org.jetbrains.annotations.Nullable Error error) {
                KLog.i("onBack");
                if (error == null) {
                    KLog.i(baseResult);
                    mPresenter.getQlcTokensInfo(baseResult, qlcAccount.getAddress());
                } else {
                    mPresenter.getQlcTokensInfo(null, qlcAccount.getAddress());
                }
            }
        });
        if (isPending) {
            KLog.i("pending中，过滤掉。。");
            return;
        }
        if (Network.isAvailable(getActivity())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        QlcClient qlcClient = new QlcClient(ConstantValue.qlcNode);
                        LedgerRpc rpc = new LedgerRpc(qlcClient);
//                        if (pending == null || pending.getInfoList() == null || pending.getInfoList().size() == 0) {
                            pending = LedgerMng.getAccountPending(qlcClient, qlcAccount.getAddress());
                            KLog.i(pending.getInfoList());
//                        }
                        if (pending == null || pending.getInfoList() == null || pending.getInfoList().size() == 0) {
                            KLog.i("没有pendding" );
                            return;
                        }
                        Iterator<Pending.PendingInfo> iterator = pending.getInfoList().iterator();
                        KLog.i("pending的数量为：" + pending.getInfoList().size());
                        while (iterator.hasNext()) {
                            Pending.PendingInfo pendingInfo = iterator.next();
                            if (errorTxid.contains(pendingInfo.getHash()) || pendingInfo.getTokenName().equals("QLC")) {
                                iterator.remove();
                            }

                        }
                        if (pending == null || pending.getInfoList() == null || pending.getInfoList().size() == 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvPending.setText("");
                                }
                            });
                            return;
                        }
                        KLog.i("pending信息为" + pending.getInfoList().get(0).getHash());
                        if (pending.getInfoList().size() != 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvPending.setText("pending: " + pending.getInfoList().size() + "");
                                }
                            });

                            isPending = true;
                            QlcReceiveUtilsKt.recevive(qlcClient, Helper.hexStringToBytes(pending.getInfoList().get(0).getHash()), qlcAccount, rpc, new ReceiveBack() {
                                @Override
                                public void recevie(boolean suceess) {
                                    if (suceess) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                isPending = false;
                                                if (pending != null && pending.getInfoList() != null && pending.getInfoList().size() != 0) {
//                                                    pending.getInfoList().remove(0);
                                                    tvPending.setText("pending: " + pending.getInfoList().size() + "");
                                                    if (pending.getInfoList().size() == 0) {
                                                        tvPending.setText("");
                                                    }
                                                    initData();
                                                } else {
                                                    tvPending.setText("");
                                                }
                                            }
                                        });
                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                isPending = false;
                                                initData();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvPending.setText("");
                                }
                            });
                            isPending = false;
                        }
                    } catch (MalformedURLException e) {
                        KLog.i("获取pendding出错" + e.getMessage());
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isPending = false;
                                initData();
                            }
                        });
                    }catch (IOException e){
                        KLog.i("获取pendding出错" + e.getMessage());
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isPending = false;
                                initData();
                            }
                        });
                    }
                }
            }).start();
        }

    }

    /**
     * 判断这个txid是否是不能得到qgas的txid
     */
    private String isContantsTxid() {
        return FileUtil.getJson(getActivity(), "area.json");
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
        AllWallet allWallet = new AllWallet();
        allWallet.setWalletType(AllWallet.WalletType.NeoWallet);
        allWallet.setWallet(wallet);
        allWallet.setWalletName(wallet.getName());
        allWallet.setWalletAddress(wallet.getAddress());
        currentSelectWallet = allWallet;
        viewModel.allWalletMutableLiveData.postValue(currentSelectWallet);
        allWalletMoney.put(allWallet, 0d);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    mPresenter.getNeoWalletDetail(wallet.getAddress(), infoMap);
                    Thread.sleep(1000);
                    queryGas(wallet.getAddress());
//                    Thread.sleep(1000);
//                    mPresenter.getWinqGas(wallet.getAddress());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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

    @OnClick({R.id.tvWalletName, R.id.tvWalletAddress, R.id.cardView, R.id.tvClaim, R.id.llResouces, R.id.llStake, R.id.iv_avater, R.id.rlWallet, R.id.ivQRCode, R.id.llNeoQlcTransfer, R.id.llEthQlcTransfer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvWalletName:
//                getGas();
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
                    startActivityForResult(new Intent(getActivity(), WalletDetailActivity.class).putExtra("ethwallet", currentSelectWallet.getEthWallet()).putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal()), 1);
                } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
                    startActivityForResult(new Intent(getActivity(), WalletDetailActivity.class).putExtra("neowallet", currentSelectWallet.getWallet()).putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal()), 1);
                } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EosWallet) {
                    startActivityForResult(new Intent(getActivity(), WalletDetailActivity.class).putExtra("eoswallet", currentSelectWallet.getEosAccount()).putExtra("walletType", AllWallet.WalletType.EosWallet.ordinal()), 1);
                } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.QlcWallet) {
                    startActivityForResult(new Intent(getActivity(), WalletDetailActivity.class).putExtra("qlcwallet", currentSelectWallet.getQlcAccount()).putExtra("walletType", AllWallet.WalletType.QlcWallet.ordinal()), 1);
                }
                break;
//            case R.id.ivClaim:
//                queryWinqGas();
//                break;
            case R.id.tvClaim:
                getGas();
                break;
//            case R.id.tvWalletGas:
//                queryWinqGas();
//                break;
            case R.id.llResouces:
                startActivity(new Intent(getActivity(), EosResourceManagementActivity.class).putExtra("eosAccount", currentSelectWallet.getEosAccount()));
                break;
            case R.id.llStake:
                FireBaseUtils.logEvent(getActivity(), FireBaseUtils.Topup_GroupPlan_Stake);
                startActivity(new Intent(getActivity(), MyStakeActivity.class));
                break;
            case R.id.iv_avater:
                if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.EthWallet) {
                    QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getEthWallet().getAddress(), getString(R.string.eth_receivable_address), "eth", 2);
                    startActivity(new Intent(getActivity(), WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.NeoWallet) {
                    QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getWallet().getAddress(), getString(R.string.neo_receivable_address), "neo", 1);
                    startActivity(new Intent(getActivity(), WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.EosWallet) {
                    QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getEosAccount().getAccountName(), getString(R.string.eos_receivable_address), "eos", 3);
                    startActivity(new Intent(getActivity(), WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                } else if (viewModel.walletTypeMutableLiveData.getValue() == AllWallet.WalletType.QlcWallet) {
                    QrEntity qrEntity = new QrEntity(viewModel.allWalletMutableLiveData.getValue().getQlcAccount().getAddress(), getString(R.string.receivable_address), "qlc", 4);
                    startActivity(new Intent(getActivity(), WalletQRCodeActivity.class).putExtra("qrentity", qrEntity));
                }
                break;
            case R.id.rlWallet:
                startActivityForResult(new Intent(getActivity(), SelectWalletTypeActivity.class), 7);
                getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                break;
            case R.id.ivQRCode:
                startActivityForResult(new Intent(getActivity(), ScanQrCodeActivity.class), 5);
                break;
            case R.id.llNeoQlcTransfer:
                startActivityForResult(new Intent(getActivity(), TransformQlcActivity.class), 5);
                break;
            case R.id.llEthQlcTransfer:
                startActivityForResult(new Intent(getActivity(), EthTransformActivity.class), 5);
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
            ToastUtil.displayShortToast(getString(R.string.please_create_a_neo_wallet_first));
        }
    }

    private void queryGas(String address) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("address", address);
        mPresenter.getNeoGasClaim(infoMap);
    }

    private void getGas() {
        showProgressDialog();
        DWebView webview = new DWebView(getActivity());
        webview.loadUrl("file:///android_asset/contract.html");

        tvPending.postDelayed(new Runnable() {
            @Override
            public void run() {
                //fromAddress, toAddress, assetHash, amount, wif, responseCallback
                Object[] arrays = new Object[2];
//                arrays[0] = Account.INSTANCE.getWallet().getECKeyPair().exportAsWIF();
                arrays[0] = Account.INSTANCE.getWallet().getWIF();
                arrays[1] = AppConfig.instance.isMainNet;
                KLog.i("开始调用jsclaimGas。。。");
                webview.callHandler("staking.claimGas", arrays, (new OnReturnValue() {
                    // $FF: synthetic method
                    // $FF: bridge method
                    @Override
                    public void onValue(Object var1) {
                        this.onValue((org.json.JSONObject) var1);
                    }

                    public final void onValue(org.json.JSONObject retValue) {
                        StringBuilder result = (new StringBuilder()).append("call succeed,return value is ");
                        KLog.i(result.append(retValue).toString());
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                try {
                                    llGetGas.setVisibility(View.GONE);
                                    ToastUtil.displayShortToast(getString(R.string.claim_success));
//                                    if (retValue.getBoolean("claimed")) {
//                                        llGetGas.setVisibility(View.GONE);
//                                        ToastUtil.displayShortToast("Claim Success");
//                                    } else {
//                                        ToastUtil.displayShortToast("Claim failed");
//                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                queryGas(Account.INSTANCE.getWallet().getAddress());
                            }
                        });

                    }
                }));
            }
        }, 1000);

//        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
//        String txid = neoNodeRPC.claimGAS(Account.INSTANCE.getWallet(), claimData, tvGasValue.getText().toString());
//        KLog.i(txid);
//        mPresenter.claimGas(Account.INSTANCE.getWallet().getAddress(), tvGasValue.getText().toString(), txid);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            pending = null;
            tvPending.setText("");
            initData();
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            allWalletMoney.remove(currentSelectWallet);
            List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            if (neoWallets.size() != 0) {
                neoWallets.get(0).setIsCurrent(true);
                Account.INSTANCE.fromWIF(neoWallets.get(0).getWif());
                AppConfig.getInstance().getDaoSession().getWalletDao().update(neoWallets.get(0));
            } else {
                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                if (ethWallets.size() != 0) {
                    AppConfig.getInstance().getDaoSession().getEthWalletDao().update(ethWallets.get(0));
                }
            }
            pending = null;
            tvPending.setText("");
            initData();
        } else if (requestCode == 2) {
            pending = null;
            tvPending.setText("");
            initData();
        } else if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            viewModel.timeStampAllWalletInitData.postValue(Calendar.getInstance().getTimeInMillis());
        } else if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            viewModel.qrcode.postValue(data.getStringExtra("result"));
        }
    }

}