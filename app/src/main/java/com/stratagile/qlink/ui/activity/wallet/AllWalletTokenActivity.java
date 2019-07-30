package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eth.WalletDetailActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerAllWalletTokenComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletTokenContract;
import com.stratagile.qlink.ui.activity.wallet.module.AllWalletTokenModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.AllWalletTokenPresenter;
import com.stratagile.qlink.ui.adapter.wallet.TokensAdapter;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.SelectWalletView;

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
 * @date 2018/10/24 15:49:29
 */

public class AllWalletTokenActivity extends BaseActivity implements AllWalletTokenContract.View {

    @Inject
    AllWalletTokenPresenter mPresenter;
    @BindView(R.id.root_rl)
    RelativeLayout rootRl;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    ArrayList<AllWallet> allWallets = new ArrayList<>();
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.toolbar2)
    Toolbar toolbar2;
    @BindView(R.id.downCHeckView)
    SelectWalletView downCHeckView;

    public static int CREATE_WALLLET = 0;
    public static int SCAN_QRCODE = 1;
    @BindView(R.id.ivWalletAvatar)
    ImageView ivWalletAvatar;
    @BindView(R.id.tvWalletType)
    TextView tvWalletType;
    @BindView(R.id.tvWalletAddress)
    TextView tvWalletAddress;
    @BindView(R.id.tvWalletAsset)
    TextView tvWalletAsset;
    @BindView(R.id.cardView)
    CardView cardView;
    private AllWallet currentSelectWallet;

    private TokensAdapter tokensAdapter;

    private double walletAsset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_all_wallet_token);
        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
        statusBar.setLayoutParams(llp);
        statusBar.setBackgroundColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("");
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        if (ethWallets.size() != 0) {
            for (int i = 0; i < ethWallets.size(); i++) {
                AllWallet allWallet = new AllWallet();
                allWallet.setEthWallet(ethWallets.get(i));
                allWallet.setWalletType(AllWallet.WalletType.EthWallet);
                allWallet.setWalletName(ethWallets.get(i).getName());
                allWallets.add(allWallet);
                if (ethWallets.get(i).getName().equals(getIntent().getStringExtra("walletName"))) {
                    currentSelectWallet = allWallet;
//                    getEthToken(ethWallets.get(i));
                }
            }
        }
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        tokensAdapter = new TokensAdapter(null);
        recyclerView.setAdapter(tokensAdapter);
        if (neoWallets.size() != 0) {
            for (int i = 0; i < neoWallets.size(); i++) {
                AllWallet allWallet = new AllWallet();
                allWallet.setWallet(neoWallets.get(i));
                allWallet.setWalletType(AllWallet.WalletType.NeoWallet);
                allWallet.setWalletName(neoWallets.get(i).getAddress());
                allWallets.add(allWallet);
                if (neoWallets.get(i).getAddress().equals(getIntent().getStringExtra("walletName"))) {
                    currentSelectWallet = allWallet;
//                    getNeoToken(neoWallets.get(i));
                }
            }
        }
        downCHeckView.setOnItemCheckListener(new SelectWalletView.OnItemCheckListener() {
            @Override
            public void onItemCheck(AllWallet item) {
                changeSelectedWallet(item);
                currentSelectWallet = item;
                tokensAdapter.setNewData(new ArrayList<>());
                if (item.getWalletType() == AllWallet.WalletType.EthWallet) {
                    getEthToken(item.getEthWallet());
                } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet) {
                    getNeoToken(item.getWallet());
                }
            }
        });
        downCHeckView.setData(allWallets, currentSelectWallet);
        tokensAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(AllWalletTokenActivity.this, EthTransactionRecordActivity.class);
                intent.putExtra("tokenInfo", tokensAdapter.getData().get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (downCHeckView.isShow()) {
            downCHeckView.close();
        } else {
            super.onBackPressed();
        }
    }

    private void changeSelectedWallet(AllWallet allWallet) {
        if (allWallet.getWalletType() == AllWallet.WalletType.EthWallet) {
            tvWalletType.setText("Ethereum Address");
            tvWalletAddress.setText(allWallet.getEthWallet().getAddress());
            ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eth_wallet));
        } else if (allWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
            tvWalletType.setText("Neo Address");
            tvWalletAddress.setText(allWallet.getWallet().getAddress());
            ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_neo_wallet));
        }
    }

    private void getEthToken(EthWallet ethWallet) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", ethWallet.getAddress());
//        infoMap.put("token", ConstantValue.ethContractAddress);
        mPresenter.getETHWalletDetail(ethWallet.getAddress(), infoMap);
    }

    private void getNeoToken(Wallet wallet) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", wallet.getAddress());
//        infoMap.put("token", ConstantValue.ethContractAddress);
        mPresenter.getNeoWalletDetail(wallet.getAddress(), infoMap);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerAllWalletTokenComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .allWalletTokenModule(new AllWalletTokenModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(AllWalletTokenContract.AllWalletTokenContractPresenter presenter) {
        mPresenter = (AllWalletTokenPresenter) presenter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KLog.i("返回。。。");
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
    public void getNeoWalletInfoBack(ArrayList<TokenInfo> tokenInfos) {
        getTokenPrice(tokenInfos);
    }

    @Override
    public void getTokenPriceBack(ArrayList<TokenInfo> tokenInfos) {
        walletAsset = 0;
        tokensAdapter.setNewData(tokenInfos);
        for (int i = 0; i < tokenInfos.size(); i++) {
            walletAsset += tokenInfos.get(i).getTokenValue() * tokenInfos.get(i).getTokenPrice();
        }
        BigDecimal b = new BigDecimal(new Double((walletAsset)).toString());
        walletAsset = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (walletAsset != 0) {
            tvWalletAsset.setText(ConstantValue.currencyBean.getCurrencyImg() + " " + walletAsset);
        } else {
            tvWalletAsset.setText("- -");
        }
    }

    @Override
    public void getCameraPermissionSuccess() {
        startActivityForResult(new Intent(this, ScanQrCodeActivity.class), AllWalletTokenActivity.SCAN_QRCODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrcode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.qrcode) {
            mPresenter.getPermission();
        }
        return super.onOptionsItemSelected(item);
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

    @OnClick(R.id.cardView)
    public void onViewClicked() {
        if (currentSelectWallet.getWalletType() == AllWallet.WalletType.EthWallet) {
            startActivity(new Intent(this, WalletDetailActivity.class).putExtra("ethwallet", currentSelectWallet.getEthWallet()).putExtra("walletType", AllWallet.WalletType.EthWallet.ordinal()));
        } else if (currentSelectWallet.getWalletType() == AllWallet.WalletType.NeoWallet) {
            startActivity(new Intent(this, WalletDetailActivity.class).putExtra("neowallet", currentSelectWallet.getWallet()).putExtra("walletType", AllWallet.WalletType.NeoWallet.ordinal()));
        }
    }


}