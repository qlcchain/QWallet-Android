package com.stratagile.qlink.ui.activity.finance;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.newwinq.ProductDetail;
import com.stratagile.qlink.ui.activity.finance.component.DaggerProductDetailComponent;
import com.stratagile.qlink.ui.activity.finance.contract.ProductDetailContract;
import com.stratagile.qlink.ui.activity.finance.module.ProductDetailModule;
import com.stratagile.qlink.ui.activity.finance.presenter.ProductDetailPresenter;
import com.stratagile.qlink.ui.activity.main.WebViewActivity;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.DateUtil;
import com.stratagile.qlink.utils.LanguageUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.math.BigDecimal;
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
    @BindView(R.id.productPoint)
    TextView productPoint;
    @BindView(R.id.tvValueDate)
    TextView tvValueDate;
    @BindView(R.id.tvMaturityDate)
    TextView tvMaturityDate;
    @BindView(R.id.allIn)
    TextView allIn;

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
        if (ConstantValue.currentUser == null) {
            startActivity(new Intent(this, AccountActivity.class));
            finish();
            return;
        }
        if (ConstantValue.isShouldShowVertifyPassword) {
            Intent intent = new Intent(this, VerifyWalletPasswordActivity.class);
            startActivityForResult(intent, 1);
            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
            finish();
            return;
        }
        getProductDetail();
    }

    private void getProductDetail() {
        showProgressDialog();
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
        closeProgressDialog();
        title.setGravity(Gravity.CENTER);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.qlc);
        drawable.setBounds(0, 0, (int) getResources().getDimension(R.dimen.x46), (int) getResources().getDimension(R.dimen.x46));
        title.setCompoundDrawables(drawable, null, null, null);
        title.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.x20));
        if (LanguageUtil.isCN(this)) {
            title.setText(productDetail.getData().getName());
            productPoint.setText(productDetail.getData().getPoint());
        } else {
            title.setText(productDetail.getData().getNameEn());
            productPoint.setText(productDetail.getData().getPointEn());
        }
        mProductDetail = productDetail;
        tvProfit.setText(BigDecimal.valueOf(productDetail.getData().getAnnualIncomeRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "%");
        tvDayTime.setText(productDetail.getData().getTimeLimit() + "");
        tvQlcCount.setText(getString(R.string.from_) + " " + productDetail.getData().getLeastAmount() + " QLC");
        etQlcCount.setHint(getString(R.string.from_) + " " + productDetail.getData().getLeastAmount() + " QLC");
        tvValueDate.setText(DateUtil.getMaturityDate(1, this));
        tvMaturityDate.setText(DateUtil.getMaturityDate(productDetail.getData().getTimeLimit(), this));
        getQLCCount();

    }



    @Override
    public void getNeoTokensInfo(NeoWalletInfo baseBack) {
        KLog.i("获取qlc余额返回");
        for (int i = 0; i < baseBack.getData().getBalance().size(); i++) {
            if (baseBack.getData().getBalance().get(i).getAsset_symbol().toLowerCase().equals("qlc")) {
                qlcCount = BigDecimal.valueOf(baseBack.getData().getBalance().get(i).getAmount()).intValue();
            }
        }
    }

    int qlcCount;

    @Override
    public void buyQLCProductBack() {
        ToastUtil.displayShortToast(getString(R.string.Succeeded));
        finish();
    }

    @OnClick({R.id.tvConfirm, R.id.servicePrivacyPolicy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvConfirm:
                if (mProductDetail == null) {
                    getProductDetail();
                    ToastUtil.displayShortToast("please wait");
                    return;
                }
                if (!checkBox.isChecked()) {
                    ToastUtil.displayShortToast(getString(R.string.please_agree_to_the_service_agreement));
                    return;
                }
                if ("".equals(etQlcCount.getText().toString())) {
                    ToastUtil.displayShortToast(getString(R.string.leastamount) + mProductDetail.getData().getLeastAmount() + " QLC");
                    return;
                }
                if (Integer.parseInt(etQlcCount.getText().toString()) < mProductDetail.getData().getLeastAmount()) {
                    ToastUtil.displayShortToast(getString(R.string.leastamount) + mProductDetail.getData().getLeastAmount() + " QLC");
                    return;
                }
                transferQLC(Account.INSTANCE.getWallet().getAddress());
                break;
            case R.id.servicePrivacyPolicy:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "https://winq.net/disclaimer.html");
                intent.putExtra("title", "Service agreement");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getQLCCount() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", Account.INSTANCE.getWallet().getAddress());
        mPresenter.getQLCCount(infoMap);
    }

    private void transferQLC(String address) {
        String amount = etQlcCount.getText().toString().trim();
        showProgressDialog();
        KLog.i("自己的qlc钱包地址为：" + address);
        KLog.i("对方的qlc钱包地址为：" + ConstantValue.mainAddress);
//        mPresenter.getUtxo(address, new SendCallBack() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", address);
        mPresenter.getNeoWalletDetail(address, infoMap, ConstantValue.mainAddress, amount, new SendBackWithTxId() {
            @Override
            public void onSuccess(String txid) {
                Map<String, String> buyMap = new HashMap<>();
                buyMap.put("account", ConstantValue.currentUser.getAccount());
                buyMap.put("productId", getIntent().getStringExtra("productId"));
                buyMap.put("amount", amount);
                buyMap.put("addressFrom", address);
                buyMap.put("addressTo", ConstantValue.mainAddress);
                buyMap.put("hex", txid);
                buyMap.put("token", AccountUtil.getUserToken());
                mPresenter.buyQLCProduct(buyMap);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @OnClick(R.id.allIn)
    public void onViewClicked() {
        etQlcCount.setText(qlcCount + "");
        etQlcCount.setSelection(etQlcCount.getText().length());
    }
}