package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerNeoTransferComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.NeoTransferContract;
import com.stratagile.qlink.ui.activity.wallet.module.NeoTransferModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.NeoTransferPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.OwnWalletUtils;
import com.vondear.rxtools.RxDataTool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
 * @date 2018/11/06 18:16:07
 */

public class NeoTransferActivity extends BaseActivity implements NeoTransferContract.View {

    @Inject
    NeoTransferPresenter mPresenter;
    @BindView(R.id.tvNeoTokenName)
    TextView tvNeoTokenName;
    @BindView(R.id.tvNeoTokenValue)
    TextView tvNeoTokenValue;
    @BindView(R.id.etNeoTokenSendValue)
    EditText etNeoTokenSendValue;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.sendto)
    TextView sendto;
    @BindView(R.id.etNeoTokenSendAddress)
    EditText etNeoTokenSendAddress;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.Memo)
    TextView Memo;
    @BindView(R.id.etEthTokenSendMemo)
    EditText etEthTokenSendMemo;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tvSend)
    TextView tvSend;

    private TokenInfo tokenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_neo_transfer);
        ButterKnife.bind(this);
    }

    private boolean isChanged = false;//申明一个flag 来判断是否已经改变

    @Override
    protected void initData() {
        tokenInfo = getIntent().getParcelableExtra("tokenInfo");
        setTitle("Send " + tokenInfo.getTokenSymol());
        tvNeoTokenName.setText(tokenInfo.getTokenSymol());

        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", tokenInfo.getWalletAddress());
        mPresenter.getNeoWalletDetail(tokenInfo.getWalletAddress(), infoMap);
        if (tokenInfo.getTokenSymol().toLowerCase().equals("neo")) {
            etNeoTokenSendValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }
        mPresenter.getUtxo(tokenInfo.getWalletAddress(), new SendCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure() {

            }
        });

        etNeoTokenSendValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("".equals(charSequence.toString())) {
                    return;
                }
                if (".".equals(charSequence.toString())) {
                    etNeoTokenSendValue.setText("");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (isChanged){//必须在修改内容前调用
                        return;
                    }
                    if (editable != null && !"".equals(editable.toString()) && !".".equals(editable.toString())) {
                        int selectionStart;
                        int selectionEnd;
                        selectionStart = etNeoTokenSendValue.getSelectionStart();
                        selectionEnd = etNeoTokenSendValue.getSelectionEnd();
                        if (!isOnlyPointNumber(etNeoTokenSendValue.getText().toString())){
                            if(selectionStart >= 1 && selectionEnd >= selectionStart)
                            {
                                editable.delete(selectionStart - 1, selectionEnd);
                            }
                            isChanged=true;
                            etNeoTokenSendValue.setText(editable);
                        }
                        float toSendQlcCount = Float.parseFloat(editable.toString());
                        KLog.i(toSendQlcCount);
                        if (toSendQlcCount > Float.parseFloat(RxDataTool.format2Decimals(tokenInfo.getTokenValue()+ "") + "")) {
                            KLog.i(RxDataTool.format2Decimals(tokenInfo.getTokenValue() + "") + "");
                            etNeoTokenSendValue.setText(RxDataTool.format2Decimals(tokenInfo.getTokenValue() + "") + "");
                            isChanged=true;
                            etNeoTokenSendValue.setSelection((RxDataTool.format2Decimals(tokenInfo.getTokenValue() + "") + "").length());
                        } else {
                            KLog.i("进入else");
                            etNeoTokenSendValue.setSelection(editable.toString().length());
                        }
                        isChanged = false;
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                    isChanged=true;
                    etNeoTokenSendValue.setText("");
                    isChanged = false;
                }
            }
        });
    }

    public boolean isOnlyPointNumber(String number) {//保留5位小数正则
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,5}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerNeoTransferComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .neoTransferModule(new NeoTransferModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(NeoTransferContract.NeoTransferContractPresenter presenter) {
        mPresenter = (NeoTransferPresenter) presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrcode_menu, menu);
        return true;
    }

    @Override
    public void getTokenPriceBack(ArrayList<TokenInfo> tokenInfos) {
        if (tokenInfos.size() == 0) {
            return;
        }
        for (int i = 0; i < tokenInfos.size(); i++) {
            if (tokenInfos.get(i).getTokenSymol().equals(tokenInfo.getTokenSymol())) {
                tvNeoTokenValue.setText("Balance: " + tokenInfos.get(i).getTokenValue() + " " + tokenInfo.getTokenSymol());
                tokenInfo.setTokenAddress(tokenInfos.get(i).getTokenAddress());
            }
        }
    }

    @Override
    public void sendSuccess(String s) {
        ToastUtil.displayShortToast(s);
        closeProgressDialog();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.qrcode) {
            startActivityForResult(new Intent(this, ScanQrCodeActivity.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            etNeoTokenSendAddress.setText(data.getStringExtra("result"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.tvSend)
    public void onViewClicked() {
        if ("".equals(etNeoTokenSendValue.getText().toString().trim())) {
            ToastUtil.displayShortToast("illegal value");
            return;
        }
        if ("".equals(etNeoTokenSendAddress.getText().toString().trim())) {
            ToastUtil.displayShortToast("please enter neo wallet address");
            return;
        }
        if (Float.parseFloat(etNeoTokenSendValue.getText().toString()) <= 0) {
            ToastUtil.displayShortToast("illegal value");
            return;
        }

        if (tokenInfo.getWalletAddress().equals(etNeoTokenSendAddress.getText().toString())) {
            ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self));
            return;
        }
        showProgressDialog();
        if (tokenInfo.getTokenSymol().toLowerCase().equals("neo") || tokenInfo.getTokenSymol().toLowerCase().equals("gas")) {
            mPresenter.sendNeo(etNeoTokenSendValue.getText().toString(), etNeoTokenSendAddress.getText().toString(), tokenInfo);
        } else {
            mPresenter.sendNEP5Token(tokenInfo, etNeoTokenSendValue.getText().toString(), etNeoTokenSendAddress.getText().toString());
        }
    }
}