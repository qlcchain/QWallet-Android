package com.stratagile.qlink.ui.activity.seize;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.seize.component.DaggerSeizeComponent;
import com.stratagile.qlink.ui.activity.seize.contract.SeizeContract;
import com.stratagile.qlink.ui.activity.seize.module.SeizeModule;
import com.stratagile.qlink.ui.activity.seize.presenter.SeizePresenter;
import com.stratagile.qlink.ui.activity.wifi.RegisterWifiActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.RxDataTool;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: $description
 * @date 2018/04/13 10:58:53
 */

public class SeizeActivity extends BaseActivity implements SeizeContract.View {

    @Inject
    SeizePresenter mPresenter;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.tv_seize_explain)
    TextView tvSeizeExplain;
    @BindView(R.id.et_your_bet)
    EditText etYourBet;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.ll_your_bet)
    LinearLayout llYourBet;
    @BindView(R.id.et_asset_tranfer)
    EditText etAssetTranfer;
    @BindView(R.id.asset_tranfer)
    LinearLayout assetTranfer;
    @BindView(R.id.bet_tip)
    ImageView betTip;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;

    /**
     * 抢注类型，
     * 0 vpn
     * 1 wifi
     */
    private int seizeType;

    /**
     * 资产类型的名字
     * 只有  vpn  和   wifi
     */
    private String assetTypeName = "";

    private VpnEntity vpnEntity;

    private WifiEntity wifiEntity;

    private Balance mBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_seize);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        //获取钱包数据
        Map<String, String> map = new HashMap<>();
        List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (wallets == null || wallets.size() == 0) {
            ToastUtil.displayShortToast("no wallet");
            finish();
            return;
        }
        map.put("address", wallets.get(SpUtil.getInt(SeizeActivity.this, ConstantValue.currentWallet, 0)).getAddress());
        mPresenter.getBalance(map);
        seizeType = getIntent().getIntExtra("seizeType", 0);
        if (seizeType == 0) {
            assetTypeName = "VPN";
            vpnEntity = getIntent().getParcelableExtra("vpnEntity");
            etAssetTranfer.setText(vpnEntity.getAssetTranfer() + "");
        } else {
            assetTypeName = "WiFi";
            wifiEntity = getIntent().getParcelableExtra("wifiEntity");
            etAssetTranfer.setText(wifiEntity.getAssetTranfer() + "");
        }
        setTitle(getString(R.string.seize).toUpperCase(Locale.ENGLISH) + " " + assetTypeName);
        tvTip.setText(getString(R.string.seize_tip, assetTypeName));
        tvSeizeExplain.setText(getString(R.string.seize_explain, assetTypeName));
        button2.setText(getString(R.string.seize) + " " + assetTypeName + " " + getString(R.string.asset));

        etYourBet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                KLog.i("222222222");
                if ("0".equals(editable.toString())) {
                    etYourBet.setText("");
                    return;
                }
                if (mBalance == null) {
                    return;
                }
                if (editable.toString().equals("")) {
                    return;
                }
                if (editable.toString().equals(".")) {
                    etYourBet.setText("");
                    return;
                }
                try {
                    if (editable != null && !"".equals(editable.toString()) && !".".equals(editable.toString())) {
                        int selectionStart;
                        int selectionEnd;
                        selectionStart = etYourBet.getSelectionStart();
                        selectionEnd = etYourBet.getSelectionEnd();
                        if (!isOnlyPointNumber(etYourBet.getText().toString())) {
                            if (selectionStart >= 1 && selectionEnd >= selectionStart) {
                                editable.delete(selectionStart - 1, selectionEnd);
                            }
                            etYourBet.setText(editable);
                        }
                        float toSendQlcCount = Float.parseFloat(editable.toString());
                        KLog.i(toSendQlcCount);
                        if (toSendQlcCount > Float.parseFloat(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "")) {
                            KLog.i(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                            etYourBet.setText(RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "");
                            etYourBet.setSelection((RxDataTool.format2Decimals(mBalance.getData().getQLC() + "") + "").length());
                        } else {
                            etYourBet.setSelection(editable.toString().length());
                        }
                    }
                } catch (Exception e) {
                    etYourBet.setText("");
                    e.printStackTrace();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(0, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }


    @Override
    protected void setupActivityComponent() {
        DaggerSeizeComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .seizeModule(new SeizeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SeizeContract.SeizeContractPresenter presenter) {
        mPresenter = (SeizePresenter) presenter;
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
    public void onGetBalancelSuccess(Balance balance) {
        mBalance = balance;
    }

    @OnClick({R.id.bet_tip, R.id.button1, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bet_tip:
                showBetTipDialog();
                break;
            case R.id.button1:
                finish();
                break;
            case R.id.button2:
                if (mBalance == null) {
                    ToastUtil.displayShortToast(getString(R.string.please_wait));
                    //获取钱包数据
                    Map<String, String> map = new HashMap<>();
                    map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(SeizeActivity.this, ConstantValue.currentWallet, 0)).getAddress());
                    mPresenter.getBalance(map);
                    return;
                }
                if (etYourBet.getText().toString().equals("") || etYourBet.getText().toString().equals("0")) {
                    ToastUtil.displayShortToast(getString(R.string.Please_enter_your_bet));
                    return;
                }
                if (Float.parseFloat(etYourBet.getText().toString()) > Float.parseFloat(etAssetTranfer.getText().toString())) {
                    if ((Float.parseFloat(etYourBet.getText().toString()) < Float.parseFloat(mBalance.getData().getQLC() + "")) && mBalance.getData().getGAS() > 0.0001) {
                        //可以正常注册
                        if (seizeType == 0) {
                            //跳转到vpn抢注
                            //todo
                            Intent vpnIntent = new Intent(this, SeizeVpnActivity.class);
                            vpnIntent.putExtra("vpnEntity", vpnEntity);
                            vpnIntent.putExtra("seizeQlc", etYourBet.getText().toString().trim());
                            startActivity(vpnIntent);
                            finish();
                        } else {
                            //跳转到wifi抢注
                            //todo
                            Intent intent = new Intent(this, RegisterWifiActivity.class);
                            intent.putExtra("wifiInfo", wifiEntity);
                            intent.putExtra("seizeQlc", etYourBet.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
                    }
                } else {
                    //抢注册的qlc不够
                    ToastUtil.displayShortToast(getString(R.string.Your_bet_should_be_greater_than_the_current_asset_value));
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示下注的解释dialog
     */
    private void showBetTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.vpn_bettip_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);//输入标题
        tvTitle.setText(getString(R.string.THIS_s_NAME_HAS_ALREADY_BEEN_REGISTERED,assetTypeName));
        Button bt_left = view.findViewById(R.id.btn_left);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.if_some,assetTypeName,assetTypeName,assetTypeName));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                checkVpnInfo();
            }
        });
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}