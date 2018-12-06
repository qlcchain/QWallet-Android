package com.stratagile.qlink.ui.activity.eos;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.PushDatamanger;
import com.stratagile.qlink.blockchain.bean.BuyRamBean;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosBuyRamComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyRamContract;
import com.stratagile.qlink.ui.activity.eos.module.EosBuyRamModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosBuyRamPresenter;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.text.DecimalFormat;
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
 * @date 2018/12/06 14:39:06
 */

public class EosBuyRamActivity extends BaseActivity implements EosBuyRamContract.View {

    @Inject
    EosBuyRamPresenter mPresenter;
    @BindView(R.id.availableRam)
    TextView availableRam;
    @BindView(R.id.ramPrice)
    TextView ramPrice;
    @BindView(R.id.checkBoxBuy)
    SmoothCheckBox checkBoxBuy;
    @BindView(R.id.checkBoxSell)
    SmoothCheckBox checkBoxSell;
    @BindView(R.id.amountEos)
    TextView amountEos;
    @BindView(R.id.etPurchaseEos)
    EditText etPurchaseEos;
    @BindView(R.id.etRecipientAccount)
    EditText etRecipientAccount;
    @BindView(R.id.tvOpreate)
    TextView tvOpreate;

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
        setContentView(R.layout.activity_eos_buy_ram);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("RAM");
    }

    @Override
    protected void initData() {
        eosAccount = getIntent().getParcelableExtra("eosAccount");
        eosToken = getIntent().getParcelableExtra("eosToken");
        Map map = new HashMap<String, Object>();
        map.put("account", eosAccount.getAccountName());
        mPresenter.getEosResource(map);
        amountEos.setText("Balance:" + eosToken.getTokenValue() + " EOS");
        etRecipientAccount.setText(eosAccount.getAccountName());
    }

    @Override
    public void setEosResource(EosResource eosResource) {
        this.eosResource = eosResource;
        availableRam.setText(parseRam((eosResource.getData().getData().getRam().getAvailable() - eosResource.getData().getData().getRam().getUsed())) + "/" + parseRam(eosResource.getData().getData().getRam().getAvailable()));
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

    @Override
    protected void setupActivityComponent() {
        DaggerEosBuyRamComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosBuyRamModule(new EosBuyRamModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosBuyRamContract.EosBuyRamContractPresenter presenter) {
        mPresenter = (EosBuyRamPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.tvOpreate)
    public void onViewClicked() {
        buyRam();
    }

    private void buyRam() {
        BuyRamBean buyRamBean = new BuyRamBean(eosAccount.getAccountName(), etRecipientAccount.getText().toString(), 3042L);
        String buyRamBeanStr = new Gson().toJson(buyRamBean);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new PushDatamanger(EosBuyRamActivity.this, eosAccount.getOwnerPrivateKey()).buyRam(buyRamBeanStr, eosAccount.getAccountName(), result -> {
                        KLog.i(result);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}