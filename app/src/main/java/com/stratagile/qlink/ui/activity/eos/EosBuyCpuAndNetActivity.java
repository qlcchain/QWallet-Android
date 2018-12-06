package com.stratagile.qlink.ui.activity.eos;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.PushDatamanger;
import com.stratagile.qlink.blockchain.bean.StakeCpuAndNet;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosBuyCpuAndNetComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyCpuAndNetContract;
import com.stratagile.qlink.ui.activity.eos.module.EosBuyCpuAndNetModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosBuyCpuAndNetPresenter;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.eblock.eos4j.api.vo.transaction.push.TxAction;
import io.eblock.eos4j.ese.Action;
import io.eblock.eos4j.ese.DataParam;
import io.eblock.eos4j.ese.DataType;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: $description
 * @date 2018/12/05 18:03:46
 */

public class EosBuyCpuAndNetActivity extends BaseActivity implements EosBuyCpuAndNetContract.View {

    @Inject
    EosBuyCpuAndNetPresenter mPresenter;
    @BindView(R.id.stakedCpuEos)
    TextView stakedCpuEos;
    @BindView(R.id.avaliableCpu)
    TextView avaliableCpu;
    @BindView(R.id.stakedNetEos)
    TextView stakedNetEos;
    @BindView(R.id.avaliableNet)
    TextView avaliableNet;
    @BindView(R.id.checkBoxStake)
    SmoothCheckBox checkBoxStake;
    @BindView(R.id.checkBoxReclaim)
    SmoothCheckBox checkBoxReclaim;
    @BindView(R.id.eosBalance)
    TextView eosBalance;
    @BindView(R.id.cpuRation)
    TextView cpuRation;
    @BindView(R.id.netRation)
    TextView netRation;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.llStake)
    LinearLayout llStake;
    @BindView(R.id.tvOpreate)
    TextView tvOpreate;
    @BindView(R.id.etStakeEos)
    EditText etStakeEos;
    @BindView(R.id.etRecipientAccount)
    EditText etRecipientAccount;

    private EosAccount eosAccount;

    private EosResource eosResource;

    private TokenInfo eosToken;

    private int netRate = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eos_buy_cpu_and_net);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("CPU/NET");
        checkBoxReclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxReclaim.setChecked(true);
                checkBoxStake.setChecked(false);
            }
        });
        checkBoxStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxReclaim.setChecked(false);
                checkBoxStake.setChecked(true);
            }
        });
        checkBoxStake.setChecked(true);
    }

    @Override
    protected void initData() {
        eosAccount = getIntent().getParcelableExtra("eosAccount");
        eosToken = getIntent().getParcelableExtra("eosToken");
        Map map = new HashMap<String, Object>();
        map.put("account", eosAccount.getAccountName());
        mPresenter.getEosResource(map);
        eosBalance.setText("Balance:" + eosToken.getTokenValue() + " EOS");
        etRecipientAccount.setText(eosAccount.getAccountName());
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                netRate = progress;
                setCupEos();
                setNetEos();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private String setCupEos() {
        if ("".equals(etStakeEos.getText().toString().trim())) {
            return "";
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(etStakeEos.getText().toString()) * (100d - netRate) / 100);
        KLog.i(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
        return bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS";
    }

    private String setNetEos() {
        if ("".equals(etStakeEos.getText().toString().trim())) {
            return "";
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(etStakeEos.getText().toString()) * netRate / 100d);
        KLog.i(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
        return bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS";
    }



    @Override
    public void setEosResource(EosResource eosResource) {
        this.eosResource = eosResource;
        avaliableNet.setText(parseRam(eosResource.getData().getData().getNet().getAvailable()) + "/" + parseRam(eosResource.getData().getData().getNet().getMax()));
        avaliableCpu.setText(parseCpu(eosResource.getData().getData().getCpu().getAvailable()) + "/" + parseCpu(eosResource.getData().getData().getCpu().getMax()));
        stakedCpuEos.setText(eosResource.getData().getData().getStaked().getCpu_weight());
        stakedNetEos.setText(eosResource.getData().getData().getStaked().getNet_weight());
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

    @Override
    protected void setupActivityComponent() {
        DaggerEosBuyCpuAndNetComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosBuyCpuAndNetModule(new EosBuyCpuAndNetModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosBuyCpuAndNetContract.EosBuyCpuAndNetContractPresenter presenter) {
        mPresenter = (EosBuyCpuAndNetPresenter) presenter;
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
        stake();
    }

    /**
     * 抵押操作
     */
    private void stake() {
        if ("".equals(etStakeEos.getText().toString().trim())) {
            return;
        }
        StakeCpuAndNet stakeCpuAndNet = new StakeCpuAndNet();
        stakeCpuAndNet.setFrom(eosAccount.getAccountName());
        stakeCpuAndNet.setReceiver(etRecipientAccount.getText().toString().trim());
        stakeCpuAndNet.setStake_cpu_quantity(setCupEos());
        stakeCpuAndNet.setStake_net_quantity(setNetEos());
        stakeCpuAndNet.setTransfer(0);
        String stakeStr = new Gson().toJson(stakeCpuAndNet);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new PushDatamanger(EosBuyCpuAndNetActivity.this, eosAccount.getOwnerPrivateKey()).stakeCpuAndNet(stakeStr, eosAccount.getAccountName(), result -> {
                        KLog.i(result);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}