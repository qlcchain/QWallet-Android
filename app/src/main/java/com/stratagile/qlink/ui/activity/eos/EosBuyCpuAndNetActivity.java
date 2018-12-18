package com.stratagile.qlink.ui.activity.eos;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.stratagile.qlink.blockchain.bean.UnDelegatebwBean;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.eos.EosResourcePrice;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosBuyCpuAndNetComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyCpuAndNetContract;
import com.stratagile.qlink.ui.activity.eos.module.EosBuyCpuAndNetModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosBuyCpuAndNetPresenter;
import com.stratagile.qlink.utils.EosUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SmoothCheckBox;
import com.stratagile.qlink.view.SweetAlertDialog;

import java.math.BigDecimal;
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
    @BindView(R.id.cpuBalance)
    TextView cpuBalance;
    @BindView(R.id.etReclaimCpu)
    EditText etReclaimCpu;
    @BindView(R.id.netBalance)
    TextView netBalance;
    @BindView(R.id.etReclaimNet)
    EditText etReclaimNet;
    @BindView(R.id.llReclaim)
    LinearLayout llReclaim;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private EosAccount eosAccount;

    private EosResource eosResource;

    private TokenInfo eosToken;

    private int cpuRate = 85;


    private EosResourcePrice eosResourcePrice;
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
                llReclaim.setVisibility(View.VISIBLE);
                llStake.setVisibility(View.GONE);
                tvOpreate.setText("Reclaim");
            }
        });
        checkBoxStake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxReclaim.setChecked(false);
                checkBoxStake.setChecked(true);
                llReclaim.setVisibility(View.GONE);
                llStake.setVisibility(View.VISIBLE);
                tvOpreate.setText("Stake");
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
        eosBalance.setText("Balance:" + eosToken.getEosTokenValue() + " EOS");
        etRecipientAccount.setText(eosAccount.getAccountName());
        seekBar.setProgress(cpuRate);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cpuRate = progress;
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map map = new HashMap<String, Object>();
                map.put("account", eosAccount.getAccountName());
                mPresenter.getEosResource(map);
                refreshLayout.setRefreshing(false);
            }
        });

        Map map1 = new HashMap<String, Object>();
        mPresenter.getEosResourcePrice(map1);
    }

    private String setCupEos() {
        if ("".equals(etStakeEos.getText().toString().trim())) {
            return "";
        }
        if (eosResourcePrice == null) {
            return "";
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(etStakeEos.getText().toString()) * cpuRate / 100);
        cpuRation.setText("CPU ≈ " + bigDecimal.divide(BigDecimal.valueOf(eosResourcePrice.getData().getCpuPrice()), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP) + " ms");
//        KLog.i(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
        return bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS";
    }

    private String setNetEos() {
        if ("".equals(etStakeEos.getText().toString().trim())) {
            return "";
        }
        if (eosResourcePrice == null) {
            return "";
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(etStakeEos.getText().toString()) * (100d - cpuRate) / 100d);
        netRation.setText("NET ≈ " + bigDecimal.divide(BigDecimal.valueOf(eosResourcePrice.getData().getNetPrice()), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP) + " KB");
//        KLog.i(bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
        return bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS";
    }

    private String setReclaimEos(String value) {
        if (value == null || "".equals(value)) {
            value = "0.0";
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(value));
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
        cpuBalance.setText("Balance: " + eosResource.getData().getData().getCpu().getAvailable() + " us");
        netBalance.setText("Balance: " + eosResource.getData().getData().getNet().getAvailable() + " Bytes");
    }

    @Override
    public void setEosResourcePrice(EosResourcePrice eosResourcePrice) {
        this.eosResourcePrice = eosResourcePrice;
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
            return cpu + " us";
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
        if (checkBoxStake.isChecked()) {
            if ("".equals(etStakeEos.getText().toString())) {
                ToastUtil.displayShortToast("please input stake EOS");
                return;
            }
            if (Double.parseDouble(etStakeEos.getText().toString()) > Double.parseDouble(eosToken.getEosTokenValue())) {
                ToastUtil.displayShortToast("Not enough EOS");
                closeProgressDialog();
                return;
            }
        } else {
            if ("".equals(etReclaimCpu.getText().toString().trim()) && "".equals(etReclaimNet.getText().toString().trim())) {
                ToastUtil.displayShortToast("please input reclaim cpu or net");
                return;
            }
            if ("0".equals(etReclaimCpu.getText().toString().trim()) && "0".equals(etReclaimNet.getText().toString().trim())) {
                ToastUtil.displayShortToast("please input reclaim cpu or net");
                return;
            }
            if (!"".equals(etReclaimCpu.getText().toString()) && Double.parseDouble(etReclaimCpu.getText().toString()) > eosResource.getData().getData().getCpu().getAvailable()) {
                ToastUtil.displayShortToast("Not enough CPU to Reclaim");
                return;
            }
            if (!"".equals(etReclaimNet.getText().toString()) && Double.parseDouble(etReclaimNet.getText().toString()) > eosResource.getData().getData().getNet().getAvailable()) {
                ToastUtil.displayShortToast("Not enough NET to Reclaim");
                return;
            }

        }
        showPaymentDetail();
    }

    private void showTestDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_tip, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        ImageView imageView = view.findViewById(R.id.ivTitle);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.op_success));
        tvContent.setText("Success");
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        tvOpreate.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetAlertDialog.cancel();
                tvOpreate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                }, 200);
            }
        }, 2000);
    }

    /**
     * 抵押操作
     */
    private void stake() {
        if ("".equals(etStakeEos.getText().toString().trim())) {
            closeProgressDialog();
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
                    String privateKey = "";
                    String permission = "";
                    if (eosAccount.getOwnerPrivateKey() != null && !"".equals(eosAccount.getOwnerPrivateKey())) {
                        privateKey = eosAccount.getOwnerPrivateKey();
                        permission = "owner";
                    } else {
                        privateKey = eosAccount.getActivePrivateKey();
                        permission = "active";
                    }
                    new PushDatamanger(EosBuyCpuAndNetActivity.this, privateKey, permission).stakeCpuAndNet(stakeStr, eosAccount.getAccountName(), result -> {
                        KLog.i(result);
                        tvOpreate.post(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                if (result == null || "".equals(result)) {

                                } else {
                                    showTestDialog();
                                }
                            }
                        });
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showPaymentDetail() {
        View view = View.inflate(this, R.layout.payment_info_layout, null);
        TextView tvPaymentType = (TextView) view.findViewById(R.id.tvPaymentType);//输入内容
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvNext = view.findViewById(R.id.tvNext);//取消按钮
        TextView tvTo = view.findViewById(R.id.tvTo);
        TextView tvCount = view.findViewById(R.id.tvCount);
        TextView tvFrom = view.findViewById(R.id.tvFrom);

        if (checkBoxStake.isChecked()) {
            tvPaymentType.setText("Stake");
            tvCount.setText(EosUtil.setEosValue(etStakeEos.getText().toString()));
        } else {
            setUnstakeCpu();
            setUnstakeNet();
            tvCount.setText(new BigDecimal(setUnstakeCpu()).add(new BigDecimal(setUnstakeNet())).setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString() + " EOS");
            tvPaymentType.setText("Reclaim");
        }

        tvTo.setText(etRecipientAccount.getText().toString());
        tvFrom.setText(eosAccount.getAccountName());

        //取消或确定按钮监听事件处l
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        Window window = sweetAlertDialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        sweetAlertDialog.setView(view);
        sweetAlertDialog.show();
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sweetAlertDialog.cancel();
                showProgressDialog();
                if (checkBoxStake.isChecked()) {
                    stake();
                } else {
                    reClaim();
                }
            }
        });
    }

    private String setUnstakeCpu() {
        if ("".equals(etReclaimCpu.getText().toString()) || "0".equals(etReclaimCpu.getText().toString())) {
            return "0.0000";
        }
        KLog.i((new BigDecimal(etReclaimCpu.getText().toString()).divide(BigDecimal.valueOf(1000), 3, BigDecimal.ROUND_HALF_UP)));
        BigDecimal decimal = (new BigDecimal(etReclaimCpu.getText().toString()).divide(BigDecimal.valueOf(1000), 3, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(eosResourcePrice.getData().getCpuPrice())).setScale(4, BigDecimal.ROUND_HALF_UP);
        KLog.i(decimal.toPlainString() + " EOS");
        return decimal.toPlainString();
    }

    private String setUnstakeNet() {
        if ("".equals(etReclaimNet.getText().toString()) || "0".equals(etReclaimNet.getText().toString())) {
            return "0.0000";
        }
        KLog.i((new BigDecimal(etReclaimNet.getText().toString()).divide(BigDecimal.valueOf(1024), 3, BigDecimal.ROUND_HALF_UP)));
        BigDecimal decimal = (new BigDecimal(etReclaimNet.getText().toString()).divide(BigDecimal.valueOf(1024), 3, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(eosResourcePrice.getData().getNetPrice())).setScale(4, BigDecimal.ROUND_HALF_UP);
        KLog.i(decimal.toPlainString() + " EOS");
        return decimal.toPlainString();
    }

    /**
     * 赎回操作
     */
    private void reClaim() {
        if ("".equals(etReclaimCpu.getText().toString().trim()) && "".equals(etReclaimNet.getText().toString().trim())) {
            closeProgressDialog();
            return;
        }
        UnDelegatebwBean unDelegatebwBean = new UnDelegatebwBean();
        unDelegatebwBean.setFrom(eosAccount.getAccountName());
        unDelegatebwBean.setReceiver(etRecipientAccount.getText().toString().trim());
        unDelegatebwBean.setUnstake_cpu_quantity(setUnstakeCpu() + " EOS");
        unDelegatebwBean.setUnstake_net_quantity(setUnstakeNet() + " EOS");
        String unStakeStr = new Gson().toJson(unDelegatebwBean);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String privateKey = "";
                    String permission = "";
                    if (eosAccount.getOwnerPrivateKey() != null && !"".equals(eosAccount.getOwnerPrivateKey())) {
                        privateKey = eosAccount.getOwnerPrivateKey();
                        permission = "owner";
                    } else {
                        privateKey = eosAccount.getActivePrivateKey();
                        permission = "active";
                    }
                    new PushDatamanger(EosBuyCpuAndNetActivity.this, privateKey, permission).unStakeCpuAndNet(unStakeStr, eosAccount.getAccountName(), result -> {
                        KLog.i(result);
                        tvOpreate.post(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                if (result == null || "".equals(result)) {

                                } else {
                                    showTestDialog();
                                }
                            }
                        });
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}