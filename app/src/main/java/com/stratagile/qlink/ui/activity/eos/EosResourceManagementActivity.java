package com.stratagile.qlink.ui.activity.eos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.PushDatamanger;
import com.stratagile.qlink.blockchain.bean.BuyRamBean;
import com.stratagile.qlink.blockchain.bean.BuyRamBytesBean;
import com.stratagile.qlink.blockchain.bean.CreateAccountBean;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosResourceManagementComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosResourceManagementContract;
import com.stratagile.qlink.ui.activity.eos.module.EosResourceManagementModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosResourceManagementPresenter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: $description
 * @date 2018/12/04 18:08:32
 */

public class EosResourceManagementActivity extends BaseActivity implements EosResourceManagementContract.View {

    @Inject
    EosResourceManagementPresenter mPresenter;
    @BindView(R.id.createAccount)
    TextView createAccount;
    @BindView(R.id.totalAssets)
    TextView totalAssets;
    @BindView(R.id.balanceAsset)
    TextView balanceAsset;
    @BindView(R.id.reClaimAsset)
    TextView reClaimAsset;
    @BindView(R.id.stakeAsset)
    TextView stakeAsset;
    @BindView(R.id.llRam)
    LinearLayout llRam;
    @BindView(R.id.avaliableRam)
    TextView avaliableRam;
    @BindView(R.id.llCpuAndNet)
    LinearLayout llCpuAndNet;
    @BindView(R.id.avaliableCpu)
    TextView avaliableCpu;
    @BindView(R.id.avaliableNet)
    TextView avaliableNet;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private EosAccount eosAccount;

    private EosResource eosResource;

    private TokenInfo eosToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eos_resource_management);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Resources");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        showProgressDialog();
        refreshLayout.setRefreshing(false);
        eosAccount = getIntent().getParcelableExtra("eosAccount");
        Map map = new HashMap<String, Object>();
        map.put("account", eosAccount.getAccountName());
        mPresenter.getEosResource(map);

//        EosPrivateKey mOwnerKey = PublicAndPrivateKeyUtils.getPrivateKey(2)[0];
//        EosPrivateKey mActiveKey = PublicAndPrivateKeyUtils.getPrivateKey(2)[1];
//        KLog.i(mOwnerKey.toString());
//        KLog.i(mActiveKey.toString());
//        KLog.i(mOwnerKey.getPublicKey());
//        KLog.i(mActiveKey.getPublicKey());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEosResourceManagementComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosResourceManagementModule(new EosResourceManagementModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosResourceManagementContract.EosResourceManagementContractPresenter presenter) {
        mPresenter = (EosResourceManagementPresenter) presenter;
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
    public void setEosResource(EosResource eosResource) {
        closeProgressDialog();
        this.eosResource = eosResource;
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("account", eosAccount.getAccountName());
        mPresenter.getEosWalletDetail(eosAccount.getAccountName(), infoMap);
    }

    @Override
    public void getTokenPriceBack(ArrayList<TokenInfo> tokenInfos) {
        for (TokenInfo tokenInfo : tokenInfos) {
            if (tokenInfo.getTokenSymol().equals("EOS")) {
                eosToken = tokenInfo;
                break;
            }
        }
        if (eosToken != null) {
            double total = eosToken.getTokenValue();
            double staked = Double.parseDouble(eosResource.getData().getData().getStaked().getCpu_weight().replace(" EOS", "")) + Double.parseDouble(eosResource.getData().getData().getStaked().getNet_weight().replace(" EOS", ""));
            BigDecimal bigDecimal = BigDecimal.valueOf(total + staked);
            totalAssets.setText("Total Assets " + bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
            balanceAsset.setText(eosToken.getEosTokenValue() + " EOS");
            stakeAsset.setText(BigDecimal.valueOf(staked).setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
        } else {
            double total = Double.parseDouble(eosResource.getData().getData().getStaked().getCpu_weight().replace(" EOS", "")) + Double.parseDouble(eosResource.getData().getData().getStaked().getNet_weight().replace(" EOS", ""));
            totalAssets.setText("Total Assets " + total + " EOS");
            balanceAsset.setText("0.0000 EOS");
            stakeAsset.setText(total + " EOS");
        }

        avaliableRam.setText(parseRam((eosResource.getData().getData().getRam().getAvailable() - eosResource.getData().getData().getRam().getUsed())) + "/" + parseRam(eosResource.getData().getData().getRam().getAvailable()));
        avaliableNet.setText(parseRam(eosResource.getData().getData().getNet().getAvailable()) + "/" + parseRam(eosResource.getData().getData().getNet().getMax()));
        avaliableCpu.setText(parseCpu(eosResource.getData().getData().getCpu().getAvailable()) + "/" + parseCpu(eosResource.getData().getData().getCpu().getMax()));
    }

    private String parseRam(int ram) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (ram < 1024) {
            return df.format(ram) + " Bytes";
        }
        double kb = ram / 1024d;
        if (kb > 1024) {
            double mb = kb / 1024d;
            return df.format(mb) + " MB";
        } else {
            return df.format(kb) + " KB";
        }
    }

    private String parseCpu(int cpu) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (cpu < 1000) {
            return df.format(cpu) + " us";
        }
        double ms = cpu / 1000d;
        if (ms > 1000) {
            double s = ms / 1024d;
            return df.format(s) + " s";
        } else {
            return df.format(ms) + " ms";
        }
    }

    @OnClick(R.id.createAccount)
    public void onViewClicked() {
        CreateAccountBean createAccountBean = new CreateAccountBean();
        createAccountBean.setCreator(eosAccount.getAccountName());
        createAccountBean.setName("huzhipeng123");
        CreateAccountBean.OwnerBean ownerBean = new CreateAccountBean.OwnerBean();
        ownerBean.setThreshold(1);
        CreateAccountBean.KeysBean keysBean = new CreateAccountBean.KeysBean();
        keysBean.setKey("EOS4tgAYa6rEySee9dEe4JnFWczgTDCBSPK62ifJfAhuRaZ3Pgc1D");
        keysBean.setWeight(1);
        ownerBean.setKeys(new ArrayList<>());
        ownerBean.getKeys().add(keysBean);
        ownerBean.setAccounts(new ArrayList<>());
        ownerBean.setWaits(new ArrayList<>());
        createAccountBean.setOwner(ownerBean);

        CreateAccountBean.ActiveBean activeBean = new CreateAccountBean.ActiveBean();
        activeBean.setThreshold(1);
        CreateAccountBean.KeysBean activeKey = new CreateAccountBean.KeysBean();
        activeKey.setWeight(1);
        activeKey.setKey("EOS7gj7mB78GhJ7e7qUy5vVfeTsUXR7m96kZsAgHwe4xGgfxhiCkT");
        activeBean.setKeys(new ArrayList<>());
        activeBean.getKeys().add(activeKey);
        activeBean.setAccounts(new ArrayList<>());
        activeBean.setWaits(new ArrayList<>());
        createAccountBean.setActive(activeBean);
        String message = new Gson().toJson(createAccountBean);
        KLog.i(message);

        BuyRamBytesBean buyRamBean = new BuyRamBytesBean(eosAccount.getAccountName(), createAccountBean.getName(), 3042L);
        String buyRamBeanStr = new Gson().toJson(buyRamBean);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    new PushDatamanger(EosResourceManagementActivity.this, eosAccount.getOwnerPrivateKey()).createAccount(message, buyRamBeanStr, eosAccount.getAccountName(), result -> {
//                        KLog.i(result);
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    @OnClick({R.id.llRam, R.id.llCpuAndNet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llRam:
                if (eosToken == null) {
                    initData();
                    return;
                }
                startActivityForResult(new Intent(this, EosBuyRamActivity.class).putExtra("eosAccount", eosAccount).putExtra("eosToken", eosToken), 0);
                break;
            case R.id.llCpuAndNet:
                if (eosToken == null) {
                    initData();
                    return;
                }
                startActivityForResult(new Intent(this, EosBuyCpuAndNetActivity.class).putExtra("eosAccount", eosAccount).putExtra("eosToken", eosToken), 0);
                break;
            default:
                break;
        }
    }
}