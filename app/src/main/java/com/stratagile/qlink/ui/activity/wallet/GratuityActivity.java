package com.stratagile.qlink.ui.activity.wallet;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerGratuityComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.GratuityContract;
import com.stratagile.qlink.ui.activity.wallet.module.GratuityModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.GratuityPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.RxDataTool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/02/02 16:19:02
 */

public class GratuityActivity extends BaseActivity implements GratuityContract.View {

    @Inject
    GratuityPresenter mPresenter;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.et_count_qlc)
    EditText etCountQlc;
    @BindView(R.id.et_dashang_qlc)
    EditText etDashangQlc;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    private Record.DataBean dataBean;
    private Balance balance;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_gratuity);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        etDashangQlc.requestFocus();
        dataBean = getIntent().getParcelableExtra("dataBean");
        Map<String, String> map = new HashMap<>();
        wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
        map.put("address", wallet.getAddress());
        mPresenter.getBalance(map);
        etDashangQlc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ("0".equals(editable.toString())) {
                    etDashangQlc.setText("");
                    return;
                }
                if (balance == null) {
                    return;
                }
                if (editable.toString().equals("")) {
                    return;
                }
                if (editable.toString().equals(".")) {
                    etDashangQlc.setText("");
                    return;
                }
                try {
                    if (balance == null) {
                        return;
                    }
                    if (editable != null && !"".equals(editable.toString()) && !".".equals(editable.toString())) {
                        int selectionStart;
                        int selectionEnd;
                        selectionStart = etDashangQlc.getSelectionStart();
                        selectionEnd = etDashangQlc.getSelectionEnd();
                        if (!isOnlyPointNumber(etDashangQlc.getText().toString())){
                            if(selectionStart >= 1 && selectionEnd >= selectionStart)
                            {
                                editable.delete(selectionStart - 1, selectionEnd);
                            }
                            etDashangQlc.setText(editable);
                        }
                        float toSendQlcCount = Float.parseFloat(editable.toString());
                        KLog.i(toSendQlcCount);
                        if (toSendQlcCount > Float.parseFloat(RxDataTool.format2Decimals(balance.getData().getQLC() + "") + "")) {
                            KLog.i(RxDataTool.format2Decimals(balance.getData().getQLC() + "") + "");
                            etDashangQlc.setText(RxDataTool.format2Decimals(balance.getData().getQLC() + "") + "");
                            etDashangQlc.setSelection((RxDataTool.format2Decimals(balance.getData().getQLC() + "") + "").length());
                        } else {
                            etDashangQlc.setSelection(editable.toString().length());
                        }
                    }
                }catch (Exception e)
                {
                    etDashangQlc.setText("");
                }
            }
        });
//        etDashangQlc.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if ("".equals(charSequence.toString())) {
//                    return;
//                }
//                if ("0".equals(charSequence.toString()) || ".".equals(charSequence.toString())) {
//                    etDashangQlc.setText("");
//                    return;
//                }
//                if (balance == null) {
//                    etDashangQlc.setText("");
//                    return;
//                }
//                if (Float.parseFloat(charSequence.toString()) > Float.parseFloat(balance.getData().getQLC())) {
//                    etDashangQlc.setText(balance.getData().getQLC() + "");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    public boolean isOnlyPointNumber(String number) {//保留两位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }


    @Override
    protected void setupActivityComponent() {
        DaggerGratuityComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .gratuityModule(new GratuityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(GratuityContract.GratuityContractPresenter presenter) {
        mPresenter = (GratuityPresenter) presenter;
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
        this.balance = balance;
        etDashangQlc.setText("");
        etCountQlc.setText(balance.getData().getQLC() + "");
    }

    @Override
    public void rewardBack(Reward reward) {
        ToastUtil.displayShortToast(getString(R.string.reward_success));
//        LogUtil.addLog("wifi打赏成功，打赏的qlc为：" + reward.getData().getQlc(), getClass().getSimpleName());
//        LogUtil.addLog("打赏的记录Id为：" + reward.getData().getRecordId(), getClass().getSimpleName());
//        if (reward.getData().getIsGratuity().equals("rewardSuccess")) {
//            int friendNum = qlinkcom.GetFriendNumInFriendlist(reward.getData().getToP2pId());
//            byte[] publicKey = new byte[100];
//            if (qlinkcom.GetFriendP2PPublicKey(reward.getData().getToP2pId(), publicKey) == 0) {
//                KLog.i(new String(publicKey).trim());
//            }
//            if(friendNum>= 0)
//            {
//                LogUtil.addLog("打赏的好友friendNum为：" + friendNum +"金额为：" + reward.getData().getQlc(), getClass().getSimpleName());
//                //打赏通知
//                Qsdk.getInstance().gratuitySuccess(new String(publicKey).trim(),reward.getData().getQlc());
//            }
//        }
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.bt_back, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etDashangQlc.getWindowToken(), 0);
                finish();
                break;
            case R.id.bt_confirm:
                if (balance == null || "".equals(etDashangQlc.getText().toString()) || "".equals(balance.getData().getQLC())) {
                    return;
                }
                if (Float.parseFloat(etDashangQlc.getText().toString()) > Float.parseFloat(balance.getData().getQLC() + "") || balance.getData().getGAS() <=0.0001) {
                    ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
                    return;
                }
                InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(etDashangQlc.getWindowToken(), 0);
                preDaShang();
                break;
            default:
                break;
        }
    }

    private void preDaShang() {
        if (etDashangQlc.getText().toString().trim().equals("")) {

            return;
        }
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().queryBuilder().list();
        Wallet wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("assetName", dataBean.getWifiName());
        infoMap.put("type", 0);
        infoMap.put("addressFrom", wallet.getAddress());
        infoMap.put("addressTo", dataBean.getAddressTo());
        infoMap.put("qlc", etDashangQlc.getText() + "");
        infoMap.put("recordId", dataBean.getRecordId());
        infoMap.put("fromP2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("toP2pId", dataBean.getToP2pId());
        mPresenter.reward(infoMap, wallet, etDashangQlc.getText().toString(), dataBean.getAddressTo());
    }

}