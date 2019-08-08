package com.stratagile.qlink.ui.activity.eos;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.PushDatamanger;
import com.stratagile.qlink.blockchain.bean.BuyRamBean;
import com.stratagile.qlink.blockchain.bean.SellRamBean;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.eos.EosResourcePrice;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosBuyRamComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosBuyRamContract;
import com.stratagile.qlink.ui.activity.eos.module.EosBuyRamModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosBuyRamPresenter;
import com.stratagile.qlink.utils.EosUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.DecimalDigitsInputFilter;
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

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

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
    @BindView(R.id.tvRamTitle)
    TextView tvRamTitle;
    @BindView(R.id.tvTip)
    TextView tvTip;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private EosAccount eosAccount;

    private EosResource eosResource;

    private TokenInfo eosToken;

    private EosResourcePrice eosResourcePrice;

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

        checkBoxBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxBuy.setChecked(true);
                checkBoxSell.setChecked(false);
                tvOpreate.setText(getString(R.string.buy));
                tvTip.setVisibility(View.VISIBLE);
                tvRamTitle.setText(getString(R.string.purchase_amount));
                amountEos.setText(getString(R.string.balance) + " " + eosToken.getEosTokenValue() + " EOS");
                etPurchaseEos.setText("");
                etPurchaseEos.setHint(getString(R.string.input_eos_account));
                etPurchaseEos.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4)});
                etPurchaseEos.setInputType(TYPE_CLASS_NUMBER |TYPE_NUMBER_FLAG_DECIMAL);
            }
        });
        checkBoxSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBoxBuy.setChecked(false);
                checkBoxSell.setChecked(true);
                tvOpreate.setText(getString(R.string.sell));
                tvTip.setVisibility(View.INVISIBLE);
                tvRamTitle.setText(getString(R.string.sell_amount_bytes));
                etPurchaseEos.setHint(getString(R.string.input_ram_amount));
                etPurchaseEos.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(0)});
                etPurchaseEos.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                etPurchaseEos.setText("");
                if (eosResource != null) {
                    amountEos.setText(getString(R.string.balance) + " " + (eosResource.getData().getData().getRam().getAvailable() - eosResource.getData().getData().getRam().getUsed()) + " bytes");
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                Map map = new HashMap<String, Object>();
                map.put("account", eosAccount.getAccountName());
                mPresenter.getEosResource(map);
            }
        });
    }

    @Override
    protected void initData() {
        eosAccount = getIntent().getParcelableExtra("eosAccount");
        eosToken = getIntent().getParcelableExtra("eosToken");
        checkBoxBuy.setChecked(true);
        amountEos.setText(getString(R.string.balance) + " " + eosToken.getEosTokenValue() + " EOS");
        Map map = new HashMap<String, Object>();
        map.put("account", eosAccount.getAccountName());
        mPresenter.getEosResource(map);
        etRecipientAccount.setText(eosAccount.getAccountName());
        etPurchaseEos.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4)});

        Map map1 = new HashMap<String, Object>();
        mPresenter.getEosResourcePrice(map1);
    }

    @Override
    public void setEosResource(EosResource eosResource) {
        this.eosResource = eosResource;
        availableRam.setText(parseRam((eosResource.getData().getData().getRam().getAvailable() - eosResource.getData().getData().getRam().getUsed())) + "/" + parseRam(eosResource.getData().getData().getRam().getAvailable()));
        if (checkBoxSell.isChecked()) {
            amountEos.setText(getString(R.string.balance) + " " + (eosResource.getData().getData().getRam().getAvailable() - eosResource.getData().getData().getRam().getUsed()) + " Bytes");
        }
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
        if ("".equals(etPurchaseEos.getText().toString()) || "0".equals(etPurchaseEos.getText().toString())) {
            return;
        }
        showPaymentDetail();
    }

    private void showPaymentDetail() {
        View view = View.inflate(this, R.layout.payment_info_layout, null);
        TextView tvPaymentType = (TextView) view.findViewById(R.id.tvPaymentType);//输入内容
        ImageView ivClose = view.findViewById(R.id.ivClose);
        TextView tvNext = view.findViewById(R.id.tvNext);//取消按钮
        TextView tvTo = view.findViewById(R.id.tvTo);
        TextView tvFrom = view.findViewById(R.id.tvFrom);

        TextView tvCount = view.findViewById(R.id.tvCount);

        if (checkBoxBuy.isChecked()) {
            tvPaymentType.setText(getString(R.string.purchase_ram));
            tvCount.setText(EosUtil.setEosValue(etPurchaseEos.getText().toString()));
        } else {
            String sellEos = BigDecimal.valueOf(eosResourcePrice.getData().getRamPrice()).multiply(new BigDecimal(etPurchaseEos.getText().toString())).divide(BigDecimal.valueOf(1024)).setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
            KLog.i(sellEos);
            tvPaymentType.setText(getString(R.string.sell) + " RAM");
            tvCount.setText(sellEos + " EOS");
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
                if (checkBoxBuy.isChecked()) {
                    buyRam();
                } else {
                    sellRam();
                }
            }
        });
    }

    private void showTestDialog() {
        View view = getLayoutInflater().inflate(R.layout.alert_dialog_tip, null, false);
        TextView tvContent = view.findViewById(R.id.tvContent);
        ImageView imageView = view.findViewById(R.id.ivTitle);
        imageView.setImageDrawable(getResources().getDrawable(R.mipmap.op_success));
        tvContent.setText(getString(R.string.success));
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

    private void buyRam() {
        if (Double.parseDouble(etPurchaseEos.getText().toString()) > Double.parseDouble(eosToken.getEosTokenValue())) {
            ToastUtil.displayShortToast(getString(R.string.not_enough) + " EOS");
            closeProgressDialog();
            return;
        }
        BuyRamBean buyRamBean = new BuyRamBean(eosAccount.getAccountName(), etRecipientAccount.getText().toString(), EosUtil.setEosValue(etPurchaseEos.getText().toString()));
        String buyRamBeanStr = new Gson().toJson(buyRamBean);
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
                    new PushDatamanger(EosBuyRamActivity.this, privateKey, permission).buyRam(buyRamBeanStr, eosAccount.getAccountName(), result -> {
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

    private void sellRam() {
        if (Double.parseDouble(etPurchaseEos.getText().toString()) > (eosResource.getData().getData().getRam().getAvailable() - eosResource.getData().getData().getRam().getUsed())) {
            ToastUtil.displayShortToast(getString(R.string.not_enough) + " RAM");
            closeProgressDialog();
            return;
        }
        String sellEos = BigDecimal.valueOf(eosResourcePrice.getData().getRamPrice()).multiply(new BigDecimal(etPurchaseEos.getText().toString())).divide(BigDecimal.valueOf(1000)).setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
        KLog.i(sellEos);
        SellRamBean sellRamBean = new SellRamBean();
        sellRamBean.setAccount(eosAccount.getAccountName());
        sellRamBean.setBytes(Long.parseLong(etPurchaseEos.getText().toString()));
        String sellRamStr = new Gson().toJson(sellRamBean);
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
                    new PushDatamanger(EosBuyRamActivity.this, privateKey, permission).sellRam(sellRamStr, eosAccount.getAccountName(), result -> {
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