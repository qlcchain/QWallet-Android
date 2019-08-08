package com.stratagile.qlink.ui.activity.eos;

import android.content.Intent;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.blockchain.bean.TransferEosMessageBean;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EosAccountDao;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.eos.component.DaggerEosTransferComponent;
import com.stratagile.qlink.ui.activity.eos.contract.EosTransferContract;
import com.stratagile.qlink.ui.activity.eos.module.EosTransferModule;
import com.stratagile.qlink.ui.activity.eos.presenter.EosTransferPresenter;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.utils.EosUtil;
import com.stratagile.qlink.utils.PopWindowUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.CustomPopWindow;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.stratagile.qlink.blockchain.EoscDataManager;
import com.stratagile.qlink.view.DecimalDigitsInputFilter;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eos
 * @Description: $description
 * @date 2018/11/27 14:27:47
 */

public class EosTransferActivity extends BaseActivity implements EosTransferContract.View {

    @Inject
    EosTransferPresenter mPresenter;
    @BindView(R.id.tvEosTokenName)
    TextView tvEosTokenName;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.tvEosTokenValue)
    TextView tvEosTokenValue;
    @BindView(R.id.etEosTokenSendValue)
    EditText etEosTokenSendValue;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.sendto)
    TextView sendto;
    @BindView(R.id.etEosTokenSendAddress)
    EditText etEosTokenSendAddress;
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

    private ArrayList<TokenInfo> tokenInfoArrayList;

    private ArrayList<String> list;

    private EosAccount eosAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        mainColor = R.color.white;
        setContentView(R.layout.activity_eos_transfer);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        tokenInfoArrayList = getIntent().getParcelableArrayListExtra("tokens");
        if (getIntent().hasExtra("tokenInfo")) {
            tokenInfo = getIntent().getParcelableExtra("tokenInfo");
        } else {
            tokenInfo = tokenInfoArrayList.get(0);
        }
        if (getIntent().hasExtra("walletAddress")) {
            etEosTokenSendAddress.setText(getIntent().getStringExtra("walletAddress"));
        }
        for (int i = 0; i < tokenInfoArrayList.size(); i++) {
            list.add(tokenInfoArrayList.get(i).getTokenSymol());
        }
        setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
        tvEosTokenName.setText(tokenInfo.getTokenSymol());
        tvEosTokenValue.setText(getString(R.string.balance) + " " + tokenInfo.getEosTokenValue());
        eosAccount = AppConfig.getInstance().getDaoSession().getEosAccountDao().queryBuilder().where(EosAccountDao.Properties.AccountName.eq(tokenInfo.getWalletAddress())).unique();

        etEosTokenSendValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(getDot())});
    }

    private int getDot() {
        if (!tokenInfo.getEosTokenValue().contains(".")) {
            etEosTokenSendValue.setInputType(TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            return 0;
        } else {
            etEosTokenSendValue.setInputType(TYPE_CLASS_NUMBER |TYPE_NUMBER_FLAG_DECIMAL);
            int dot = tokenInfo.getEosTokenValue().length() - tokenInfo.getEosTokenValue().indexOf(".") - 1;
            KLog.i(dot);
            return dot;
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEosTransferComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .eosTransferModule(new EosTransferModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EosTransferContract.EosTransferContractPresenter presenter) {
        mPresenter = (EosTransferPresenter) presenter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrcode_menu, menu);
        return true;
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
            etEosTokenSendAddress.setText(data.getStringExtra("result"));
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

    @OnClick({R.id.tvEosTokenName, R.id.tvSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvEosTokenName:
                showSpinnerPopWindow();
                break;
            case R.id.tvSend:
                if (etEosTokenSendAddress.getText().toString().equals("")) {
                    ToastUtil.displayShortToast(getString(R.string.wallet_address_error));
                    return;
                }
                if (!EosUtil.isEosName(etEosTokenSendAddress.getText().toString().trim())) {
                    ToastUtil.displayShortToast(getString(R.string.wallet_address_error));
                    return;
                }
                if (etEosTokenSendAddress.getText().toString().toLowerCase().equals(tokenInfo.getWalletAddress().toLowerCase())) {
                    ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self));
                    return;
                }
                if (etEosTokenSendValue.getText().toString().equals("")) {
                    ToastUtil.displayShortToast(getString(R.string.illegal_value));
                    return;
                }
                if (etEosTokenSendValue.getText().toString().equals("0")) {
                    ToastUtil.displayShortToast(getString(R.string.illegal_value));
                    return;
                }
                if (Double.parseDouble(etEosTokenSendValue.getText().toString()) > Double.parseDouble(tokenInfo.getEosTokenValue())) {
                    ToastUtil.displayShortToast(getString(R.string.not_enough) + " " + tokenInfo.getTokenSymol());
                    return;
                }
                String privateKey = "";
                String permission = "";
                if (eosAccount.getOwnerPrivateKey() != null && !"".equals(eosAccount.getOwnerPrivateKey())) {
                    privateKey = eosAccount.getOwnerPrivateKey();
                    permission = "owner";
                } else {
                    privateKey = eosAccount.getActivePrivateKey();
                    permission = "active";
                }
                transfer(privateKey, permission, tokenInfo.getTokenAddress(), tokenInfo.getWalletAddress(), etEosTokenSendAddress.getText().toString(), etEosTokenSendValue.getText().toString(), etEthTokenSendMemo.getText().toString(), tokenInfo.getTokenSymol());
                break;
            default:
                break;
        }
    }

    private void showSpinnerPopWindow() {
        PopWindowUtil.INSTANCE.showSharePopWindow(this, ivArrow, list, new PopWindowUtil.OnItemSelectListener() {
            @Override
            public void onSelect(@NotNull String content) {
                if (!"".equals(content)) {
                    for (int i = 0; i < tokenInfoArrayList.size(); i++) {
                        if (tokenInfoArrayList.get(i).getTokenSymol().equals(content)) {
                            tokenInfo = tokenInfoArrayList.get(i);
                            setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
                            tvEosTokenName.setText(tokenInfo.getTokenSymol());
                            etEosTokenSendValue.setText("");
                            etEosTokenSendValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(getDot())});
                            tvEosTokenValue.setText(getString(R.string.balance) + " " + tokenInfo.getEosTokenValue());
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tvEosTokenName.getWidth(), (int) getResources().getDimension(R.dimen.x1));
                            viewLine.setLayoutParams(layoutParams);
                        }
                    }
                }
                SpringAnimationUtil.endRotatoSpringViewAnimation(ivArrow, new DynamicAnimation.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                    }
                });
            }
        });
        SpringAnimationUtil.startRotatoSpringViewAnimation(ivArrow, new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

            }
        });
    }

    private String setTransferTokenValue(String value, String token) {
        BigDecimal bigDecimal = BigDecimal.valueOf(Double.parseDouble(value));
        KLog.i(bigDecimal.setScale(getDot(), BigDecimal.ROUND_HALF_UP).toPlainString() + " " + token);
        return bigDecimal.setScale(getDot(), BigDecimal.ROUND_HALF_UP).toPlainString() + " " + token;
    }

    private void transfer(String privateKey, String permission, String contractAccount, String from, String to, String quantity, String memo, String token) {
        showProgressDialog();
        System.out.println("============= 转账EOS ===============");
        KLog.i(privateKey);
        KLog.i(contractAccount);
        KLog.i(from);
        KLog.i(to);
        KLog.i(quantity);
        KLog.i(memo);
        KLog.i(token);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message = new Gson().toJson(new TransferEosMessageBean(memo, to, setTransferTokenValue(quantity, token), from));
                KLog.i(message);
                new EoscDataManager(EosTransferActivity.this, privateKey, contractAccount, permission).pushAction(message, from, txid -> {
                    tvSend.post(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                        }
                    });
                    if (txid == null || txid.equals("")) {
                        ToastUtil.displayShortToast(getString(R.string.transfer_error));
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.transfer_success));
                        finish();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (CustomPopWindow.onBackPressed()) {
            SpringAnimationUtil.endRotatoSpringViewAnimation(ivArrow, new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                }
            });
        } else {
            super.onBackPressed();
        }
    }
}