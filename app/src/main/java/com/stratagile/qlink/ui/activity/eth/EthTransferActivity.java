package com.stratagile.qlink.ui.activity.eth;

import android.content.Intent;
import android.os.Bundle;
import android.support.animation.DynamicAnimation;
import android.support.constraint.Group;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthTransferComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthTransferContract;
import com.stratagile.qlink.ui.activity.eth.module.EthTransferModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthTransferPresenter;
import com.stratagile.qlink.utils.PopWindowUtil;
import com.stratagile.qlink.utils.SpringAnimationUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.CustomPopWindow;
import com.stratagile.qlink.view.SpinnerPopWindow;

import org.jetbrains.annotations.NotNull;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/31 10:17:17
 */

public class EthTransferActivity extends BaseActivity implements EthTransferContract.View {

    @Inject
    EthTransferPresenter mPresenter;
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
    @BindView(R.id.tvEthTokenName)
    TextView tvEthTokenName;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;

    private TokenInfo tokenInfo;

    private boolean isOpen = false;

    private int costLevel = 1;

    /**
     * eth当前的市价
     */
    private double ethPrice;

    private int gasLimit = 60000;

    private int gasPrice = 0;

    private String gasEth;

    private ArrayList<TokenInfo> tokenInfoArrayList;

    private TokenInfo ethTokenInfo;

    private SpinnerPopWindow<String> mSpinerPopWindow;

    ArrayList<String> ctype = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_transfer);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        tokenInfoArrayList = getIntent().getParcelableArrayListExtra("tokens");
        for (int i = 0; i < tokenInfoArrayList.size(); i++) {
            if (tokenInfoArrayList.get(i).getTokenSymol().toLowerCase().equals("eth")) {
                ethTokenInfo = tokenInfoArrayList.get(0);
                break;
            }
        }
        if (getIntent().hasExtra("walletAddress")) {
            tokenInfo = tokenInfoArrayList.get(0);
            etEthTokenSendAddress.setText(getIntent().getStringExtra("walletAddress"));
        } else {
            tokenInfo = getIntent().getParcelableExtra("tokenInfo");
        }

        setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
        String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
        tvEthTokenValue.setText(getString(R.string.balance) + " " + value);

        tvEthTokenName.setText(tokenInfo.getTokenSymol());

        ethPrice = ethTokenInfo.getTokenPrice();
        for (int i = 0; i < tokenInfoArrayList.size(); i++) {
            ctype.add(tokenInfoArrayList.get(i).getTokenSymol());
            if (tokenInfo.getTokenSymol().equals(tokenInfoArrayList.get(i).getTokenSymol())) {
                tokenPositon = i;
            }
        }
        mSpinerPopWindow = new SpinnerPopWindow<String>(this, ctype, itemClickListener);
        mSpinerPopWindow.setOnDismissListener(dismissListener);

        gasPrice = seekBar.getProgress() + 6;
        tvGwei.setText(gasPrice + " gwei");
        BigDecimal gas = Convert.toWei(gasPrice + "", Convert.Unit.GWEI).divide(Convert.toWei(1 + "", Convert.Unit.ETHER));
        BigDecimal f = gas.multiply(new BigDecimal(gasLimit));
        BigDecimal gasMoney = f.multiply(new BigDecimal(ethPrice + ""));
        double f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
        tvCostEth.setText(gasEth + " ether ≈ " + ConstantValue.currencyBean.getCurrencyImg() + f1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gasPrice = progress + 6;
                tvGwei.setText(gasPrice + " gwei");
                BigDecimal gas = Convert.toWei(gasPrice + "", Convert.Unit.GWEI).divide(Convert.toWei(1 + "", Convert.Unit.ETHER));
                BigDecimal f = gas.multiply(new BigDecimal(gasLimit));

                BigDecimal gasMoney = f.multiply(new BigDecimal(ethPrice + ""));
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

        tvEthTokenName.post(() -> {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tvEthTokenName.getWidth(), (int) getResources().getDimension(R.dimen.x1));
            viewLine.setLayoutParams(layoutParams);
        });
    }

    /**
     * 监听popupwindow取消
     */
    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            SpringAnimationUtil.endRotatoSpringViewAnimation(ivArrow, new DynamicAnimation.OnAnimationEndListener() {
                @Override
                public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                }
            });
        }
    };

    private void showSpinnerPopWindow() {
        PopWindowUtil.INSTANCE.showSharePopWindow(this, ivArrow, ctype, new PopWindowUtil.OnItemSelectListener() {
            @Override
            public void onSelect(@NotNull String content) {
                if (!"".equals(content)) {
                    for (int i = 0; i < tokenInfoArrayList.size(); i++) {
                        if (tokenInfoArrayList.get(i).getTokenSymol().equals(content)) {
                            tokenInfo = tokenInfoArrayList.get(i);
                            setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
                            String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
                            tvEthTokenValue.setText(getString(R.string.balance) + " " + value);
                            tvEthTokenName.setText(tokenInfo.getTokenSymol());
                            etEthTokenSendValue.setText("");
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tvEthTokenName.getWidth(), (int) getResources().getDimension(R.dimen.x1));
                            viewLine.setLayoutParams(layoutParams);
                        }
                    }
                } else {
                    SpringAnimationUtil.endRotatoSpringViewAnimation(ivArrow, new DynamicAnimation.OnAnimationEndListener() {
                        @Override
                        public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

                        }
                    });
                }
            }
        });
//        mSpinerPopWindow.setWidth((tvEthTokenName.getWidth()));
//        mSpinerPopWindow.showAsDropDown(viewLine);
        SpringAnimationUtil.startRotatoSpringViewAnimation(ivArrow, new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {

            }
        });
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mSpinerPopWindow.dismiss();
            tvEthTokenValue.setText(ctype.get(position));
            for (int i = 0; i < tokenInfoArrayList.size(); i++) {
                if (tokenInfoArrayList.get(i).getTokenSymol().equals(ctype.get(position))) {
                    tokenInfo = tokenInfoArrayList.get(i);
                    setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
                    String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
                    tvEthTokenValue.setText(getString(R.string.balance) + " " + value);
                    tvEthTokenName.setText(tokenInfo.getTokenSymol());
                    etEthTokenSendValue.setText("");

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(tvEthTokenName.getWidth(), (int) getResources().getDimension(R.dimen.x1));
                    viewLine.setLayoutParams(layoutParams);
                }
            }
        }
    };

    @Override
    protected void setupActivityComponent() {
        DaggerEthTransferComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethTransferModule(new EthTransferModule(this))
                .build()
                .inject(this);
    }

    private void getEthToken(String address) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", address);
        mPresenter.getETHWalletDetail(address, infoMap);
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

    private ArrayList<EthWalletInfo.DataBean.TokensBean> tokensBeans = new ArrayList<>();


    int tokenPositon;

    @Override
    public void getEthWalletBack(EthWalletInfo ethWalletInfo) {
        tokensBeans = ethWalletInfo.getData().getTokens();
        String[] ctype = new String[1 + ethWalletInfo.getData().getTokens().size()];
        ctype[0] = "ETH";
        if (!getIntent().hasExtra("walletAddress")) {
            for (int i = 0; i < ethWalletInfo.getData().getTokens().size(); i++) {
                ctype[i + 1] = ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol();
                if (ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol().equals(tokenInfo.getTokenSymol())) {
                    tokenPositon = 1 + i;
                    TokenInfo tokenInfo1 = new TokenInfo();
                    tokenInfo1.setTokenName(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol());
                    tokenInfo1.setTokenSymol(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol());
                    tokenInfo1.setTokenAddress(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getAddress());
                    tokenInfo1.setTokenValue(ethWalletInfo.getData().getTokens().get(i).getBalance());
                    tokenInfo1.setWalletType(AllWallet.WalletType.EthWallet);
                    tokenInfo1.setWalletAddress(ethWalletInfo.getData().getAddress());
                    tokenInfo1.setMainNetToken(true);
                    tokenInfo = tokenInfo1;

                    setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
                    String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
                    tvEthTokenValue.setText(getString(R.string.balance) + " " + value);
                }
            }
        } else {
            TokenInfo tokenInfo1 = new TokenInfo();
            tokenInfo1.setTokenName("ETH");
            tokenInfo1.setTokenSymol("ETH");
            tokenInfo1.setTokenAddress(ethWalletInfo.getData().getAddress());
            if ("false".equals(ethWalletInfo.getData().getETH().getBalance().toString()) || "-1.0".equals(ethWalletInfo.getData().getETH().getBalance().toString())) {
                tokenInfo1.setTokenValue(0);
            } else {
                tokenInfo1.setTokenValue(Double.parseDouble(ethWalletInfo.getData().getETH().getBalance().toString()));
            }
            tokenInfo1.setTokenImgName("eth_eth");
            tokenInfo1.setWalletType(AllWallet.WalletType.EthWallet);
            tokenInfo1.setWalletAddress(ethWalletInfo.getData().getAddress());
            tokenInfo1.setMainNetToken(true);
            tokenInfo = tokenInfo1;
            setTitle(getString(R.string.send) + " " + tokenInfo.getTokenSymol());
            String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
            tvEthTokenValue.setText(getString(R.string.balance) + " " + value);
        }
    }


    @Override
    public void getTokenPriceBack(TokenPrice tokenPrice) {
        ethPrice = tokenPrice.getData().get(0).getPrice();
        gasPrice = seekBar.getProgress() + 6;
        tvGwei.setText(gasPrice + " gwei");
        BigDecimal gas = Convert.toWei(gasPrice + "", Convert.Unit.GWEI).divide(Convert.toWei(1 + "", Convert.Unit.ETHER));
        BigDecimal f = gas.multiply(new BigDecimal(gasLimit));
        BigDecimal gasMoney = f.multiply(new BigDecimal(ethPrice + ""));
        double f1 = gasMoney.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        gasEth = f.setScale(4, BigDecimal.ROUND_HALF_UP).toPlainString();
        tvCostEth.setText(gasEth + " ether ≈ " + ConstantValue.currencyBean.getCurrencyImg() + f1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                gasPrice = progress + 6;
                tvGwei.setText(gasPrice + " gwei");
                BigDecimal gas = Convert.toWei(gasPrice + "", Convert.Unit.GWEI).divide(Convert.toWei(1 + "", Convert.Unit.ETHER));
                BigDecimal f = gas.multiply(new BigDecimal(gasLimit));

                BigDecimal gasMoney = f.multiply(new BigDecimal(ethPrice + ""));
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
    public void sendSuccess(String s) {
        ToastUtil.displayShortToast(getResources().getString(R.string.success));
        finish();
    }

    @OnClick({R.id.llOpen, R.id.tvSend, R.id.tvEthTokenName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llOpen:
                toggleCost();
                break;
            case R.id.tvSend:
                sendEthToken();
                break;
            case R.id.tvEthTokenName:
                showSpinnerPopWindow();
                break;
            default:
                break;
        }
    }

    private void sendEthToken() {
        if (etEthTokenSendAddress.getText().toString().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.wallet_address_error));
            return;
        }
        if (!ETHWalletUtils.isETHValidAddress(etEthTokenSendAddress.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.wallet_address_error));
            return;
        }
        if (etEthTokenSendAddress.getText().toString().toLowerCase().equals(tokenInfo.getWalletAddress().toLowerCase())) {
            ToastUtil.displayShortToast(getString(R.string.can_not_send_to_self));
            return;
        }
        if (etEthTokenSendValue.getText().toString().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.illegal_value));
            return;
        }
        if (etEthTokenSendValue.getText().toString().equals("0")) {
            ToastUtil.displayShortToast(getString(R.string.illegal_value));
            return;
        }
        if (Double.parseDouble(etEthTokenSendValue.getText().toString()) > tokenInfo.getTokenValue()) {
            ToastUtil.displayShortToast(getString(R.string.not_enough) + " " + tokenInfo.getTokenSymol());
            return;
        }
        if (tokenInfo.getTokenSymol().toLowerCase().equals("eth")) {
            if (ethTokenInfo.getTokenValue() >= (Double.parseDouble(gasEth) + Double.parseDouble(etEthTokenSendValue.getText().toString().trim()))) {
                showProgressDialog();
                mPresenter.transactionEth(tokenInfo, etEthTokenSendAddress.getText().toString(), etEthTokenSendValue.getText().toString(), gasLimit, gasPrice);
            } else {
                ToastUtil.displayShortToast(getString(R.string.not_enough) + " eth" );
            }
        } else {
            if (ethTokenInfo.getTokenValue() >= Double.parseDouble(gasEth)) {
                showProgressDialog();
                mPresenter.transaction(tokenInfo, etEthTokenSendAddress.getText().toString(), etEthTokenSendValue.getText().toString(), gasLimit, gasPrice);
            } else {
                ToastUtil.displayShortToast(getString(R.string.not_enough) + " eth" );
            }
        }

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