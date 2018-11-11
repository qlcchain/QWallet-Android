package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.constraint.Group;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerEthTransferComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransferContract;
import com.stratagile.qlink.ui.activity.wallet.module.EthTransferModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.EthTransferPresenter;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.vondear.rxtools.RxTool;

import org.spongycastle.util.encoders.Hex;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jnr.ffi.Struct;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/31 10:17:17
 */

public class EthTransferActivity extends BaseActivity implements EthTransferContract.View {

    @Inject
    EthTransferPresenter mPresenter;
    @BindView(R.id.tvEthTokenName)
    TextView tvEthTokenName;
    @BindView(R.id.tvEthTokenValue)
    TextView tvEthTokenValue;
    @BindView(R.id.etEthTokenSendValue)
    EditText etEthTokenSendValue;
    @BindView(R.id.etEthTokenSendAddress)
    EditText etEthTokenSendAddress;
    @BindView(R.id.etEthTokenSendMemo)
    EditText etEthTokenSendMemo;
    @BindView(R.id.tvCostEth)
    TextView tvCostEth;
    @BindView(R.id.ivShow)
    ImageView ivShow;
    @BindView(R.id.llOpen)
    LinearLayout llOpen;
    @BindView(R.id.group)
    Group group;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    //    @BindView(R.id.tvGasFee)
//    TextView tvGasFee;
    @BindView(R.id.tvSend)
    TextView tvSend;
    @BindView(R.id.tvGwei)
    TextView tvGwei;

    private TokenInfo tokenInfo;

    private boolean isOpen = false;

    private int costLevel = 1;

    private EthWalletInfo.DataBean.ETHBean ethBean;
    private EthWalletInfo.DataBean.TokensBean tokensBean;

    private int gasLimit = 60000;

    private int gasPrice = 0;

    private String gasEth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        KLog.i(Base64.encodeToString(qlinkcom.AES("123456789".getBytes(),0),Base64.NO_WRAP));
        KLog.i(new String(qlinkcom.AES(Base64.decode("mcpyeXViSGKv9e/cuN8EOw==", Base64.NO_WRAP),1)));
        setContentView(R.layout.activity_eth_transfer);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        tokenInfo = getIntent().getParcelableExtra("tokenInfo");
        getEthToken(tokenInfo);

        HashMap<String, Object> infoMap = new HashMap<>();
        String[] tokens = new String[2];
        tokens[0] = "ETH";
        tokens[1] = tokenInfo.getTokenSymol();
        infoMap.put("symbols", tokens);
        infoMap.put("coin", ConstantValue.currencyBean.getName());
        mPresenter.getToeknPrice(infoMap);

        tvEthTokenName.setText(tokenInfo.getTokenSymol());
        setTitle("Send " + tokenInfo.getTokenSymol());
        String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
        tvEthTokenValue.setText("Balance: " + value);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gasPrice = progress + 6;
                tvGwei.setText(gasPrice + " gwei");
                BigDecimal gas = Convert.toWei(gasPrice + "", Convert.Unit.GWEI).divide(Convert.toWei(1 + "", Convert.Unit.ETHER));
                BigDecimal f = gas.multiply(new BigDecimal(gasLimit));

                BigDecimal gasMoney = f.multiply(new BigDecimal(tokenInfo.getEthPrice() + ""));
                double f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                KLog.i(f1);
                gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
                tvCostEth.setText(gasEth + " ether ≈ " + ConstantValue.currencyBean.getCurrencyImg() + f1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthTransferComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethTransferModule(new EthTransferModule(this))
                .build()
                .inject(this);
    }

    private void getEthToken(TokenInfo tokenInfo) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", tokenInfo.getWalletAddress());
        infoMap.put("token", tokenInfo.getTokenAddress());
        mPresenter.getETHWalletDetail(tokenInfo.getWalletAddress(), infoMap);
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
            etEthTokenSendAddress.setText(data.getStringExtra("result"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(EthTransferContract.EthTransferContractPresenter presenter) {
        mPresenter = (EthTransferPresenter) presenter;
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
    public void getEthWalletBack(EthWalletInfo ethWalletInfo) {
        ethBean = ethWalletInfo.getData().getETH();
        tokensBean = ethWalletInfo.getData().getTokens().get(0);
    }

    @Override
    public void getTokenPriceBack(TokenPrice tokenPrice) {
        for (int i = 0; i < tokenPrice.getData().size(); i++) {
            if (tokenPrice.getData().get(i).getSymbol().toLowerCase().equals("eth")) {
                tokenInfo.setEthPrice(tokenPrice.getData().get(i).getPrice());
            }
        }

        gasPrice = seekBar.getProgress() + 6;
        tvGwei.setText(gasPrice + " gwei");
        BigDecimal gas = Convert.toWei(gasPrice + "", Convert.Unit.GWEI).divide(Convert.toWei(1 + "", Convert.Unit.ETHER));
        BigDecimal f = gas.multiply(new BigDecimal(gasLimit));
        BigDecimal gasMoney = f.multiply(new BigDecimal(tokenInfo.getEthPrice() + ""));
        double f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
        tvCostEth.setText(gasEth + " ether ≈ " + ConstantValue.currencyBean.getCurrencyImg() + f1);
    }

    @Override
    public void sendSuccess(String s) {
        ToastUtil.displayShortToast(getResources().getString(R.string.success));
        finish();
    }

    @OnClick({R.id.llOpen, R.id.tvSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llOpen:
                toggleCost();
                break;
            case R.id.tvSend:
                sendEthToken();
                break;
            default:
                break;
        }
    }

    private void sendEthToken() {
        if (etEthTokenSendAddress.getText().toString().equals("")) {
            ToastUtil.displayShortToast("Wallet Address Error");
            return;
        }
        if (etEthTokenSendAddress.getText().toString().toLowerCase().equals(tokenInfo.getWalletAddress().toLowerCase())) {
            ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self));
            return;
        }
        if (etEthTokenSendValue.getText().toString().equals("")) {
            ToastUtil.displayShortToast("illegal value");
            return;
        }
        if (etEthTokenSendValue.getText().toString().equals("0")) {
            ToastUtil.displayShortToast("illegal value");
            return;
        }
        if (Double.parseDouble(etEthTokenSendValue.getText().toString()) > tokenInfo.getTokenValue()) {
            ToastUtil.displayShortToast("Not enough " + tokenInfo.getTokenSymol());
            return;
        }
        showProgressDialog();
        if (tokenInfo.getTokenSymol().toLowerCase().equals("eth")) {
            mPresenter.transactionEth(tokenInfo, etEthTokenSendAddress.getText().toString(), etEthTokenSendValue.getText().toString(), gasLimit, gasPrice);
        } else {
            mPresenter.transaction(tokenInfo, etEthTokenSendAddress.getText().toString(), etEthTokenSendValue.getText().toString(), gasLimit, gasPrice);
        }

    }

    private void toggleCost() {
        if (isOpen) {
            isOpen = false;
            group.setVisibility(View.GONE);
            SpringAnimationUtil.endRotatoSpringViewAnimation(ivShow, new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                }
            });
        } else {
            isOpen = true;
            group.setVisibility(View.VISIBLE);
            SpringAnimationUtil.startRotatoSpringViewAnimation(ivShow, new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                }
            });
        }
    }
}