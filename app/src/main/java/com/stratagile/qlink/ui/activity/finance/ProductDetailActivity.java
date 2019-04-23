package com.stratagile.qlink.ui.activity.finance;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.newwinq.ProductDetail;
import com.stratagile.qlink.ui.activity.finance.component.DaggerProductDetailComponent;
import com.stratagile.qlink.ui.activity.finance.contract.ProductDetailContract;
import com.stratagile.qlink.ui.activity.finance.module.ProductDetailModule;
import com.stratagile.qlink.ui.activity.finance.presenter.ProductDetailPresenter;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.RSAEncrypt;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SmoothCheckBox;
import com.vondear.rxtools.view.RxQRCode;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/11 11:16:32
 */

public class ProductDetailActivity extends BaseActivity implements ProductDetailContract.View {

    @Inject
    ProductDetailPresenter mPresenter;
    @BindView(R.id.etQlcCount)
    EditText etQlcCount;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvDayTime)
    TextView tvDayTime;
    @BindView(R.id.tvQlcCount)
    TextView tvQlcCount;
    @BindView(R.id.llRule)
    LinearLayout llRule;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;
    @BindView(R.id.servicePrivacyPolicy)
    TextView servicePrivacyPolicy;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llPayWay1)
    LinearLayout llPayWay1;
    ProductDetail mProductDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        getProductDetail();
    }

    private void getProductDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("id", getIntent().getStringExtra("productId"));
        mPresenter.getProductDetail(map);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerProductDetailComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .productDetailModule(new ProductDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ProductDetailContract.ProductDetailContractPresenter presenter) {
        mPresenter = (ProductDetailPresenter) presenter;
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
    public void showProductDetail(ProductDetail productDetail) {
        title.setGravity(Gravity.CENTER);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.qlc);
        drawable.setBounds(0, 0, (int) getResources().getDimension(R.dimen.x46), (int) getResources().getDimension(R.dimen.x46));
        title.setCompoundDrawables(drawable, null, null, null);
        title.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x20));
        title.setText(productDetail.getData().getName());
        mProductDetail = productDetail;
        tvProfit.setText(BigDecimal.valueOf(productDetail.getData().getAnnualIncomeRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        tvDayTime.setText(productDetail.getData().getTimeLimit() + "");
        tvQlcCount.setText("From " + productDetail.getData().getLeastAmount() + " QLC");
        etQlcCount.setHint("From " + productDetail.getData().getLeastAmount() + " QLC");


    }

    @Override
    public void getNeoTokensInfo(NeoWalletInfo baseBack) {

    }

    @Override
    public void buyQLCProductBack() {
        ToastUtil.displayShortToast("success");
        finish();
    }

    boolean isSelfPay = true;

    @OnClick({R.id.tvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:
                transferQLC(Account.INSTANCE.getWallet().getAddress());
                break;
            default:
                break;
        }
    }

    private void transferQLC(String address) {
        String amount = etQlcCount.getText().toString().trim();
        showProgressDialog();
        KLog.i("自己的qlc钱包地址为：" + address);
        KLog.i("对方的qlc钱包地址为：" + ConstantValue.mainAddress);
        mPresenter.getUtxo(address, new SendCallBack() {
            @Override
            public void onSuccess() {
                Map<String, String> infoMap = new HashMap<>();
                infoMap.put("address", address);
                mPresenter.getNeoWalletDetail(address, infoMap, ConstantValue.mainAddress, amount, new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        Map<String, String> buyMap = new HashMap<>();
                        buyMap.put("account", SpUtil.getString(AppConfig.getInstance(), ConstantValue.userPhone, ""));
                        buyMap.put("productId", getIntent().getStringExtra("productId"));
                        buyMap.put("amount", amount);
                        buyMap.put("addressFrom", address);
                        buyMap.put("addressTo", ConstantValue.mainAddress);
                        buyMap.put("hex", txid);

                        String orgin = Calendar.getInstance().getTimeInMillis() + "," + SpUtil.getString(AppConfig.getInstance(), ConstantValue.userPassword, "");
                        KLog.i("加密前的token为：" + orgin);
                        String token = RSAEncrypt.encrypt(orgin, SpUtil.getString(AppConfig.getInstance(), ConstantValue.userRsaPubKey, ""));
                        buyMap.put("token", token);
                        mPresenter.buyQLCProduct(buyMap);
                    }

                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }
}