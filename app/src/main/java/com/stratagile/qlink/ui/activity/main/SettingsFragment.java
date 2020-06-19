package com.stratagile.qlink.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.ui.activity.main.component.DaggerSettingsComponent;
import com.stratagile.qlink.ui.activity.main.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.main.module.SettingsModule;
import com.stratagile.qlink.ui.activity.main.presenter.SettingsPresenter;
import com.stratagile.qlink.ui.activity.setting.CurrencyUnitActivity;
import com.stratagile.qlink.ui.activity.wallet.ChangePasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.ChooseWalletActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.VersionUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/10/29 10:38:15
 */

public class SettingsFragment extends BaseFragment implements SettingsContract.View {
    @Override
    protected void initDataFromNet() {

    }
    @Inject
    SettingsPresenter mPresenter;
    @BindView(R.id.switchBar)
    SwitchButton switchBar;
    @BindView(R.id.llTouchId)
    LinearLayout llTouchId;
    @BindView(R.id.llPassWord)
    LinearLayout llPassWord;
    @BindView(R.id.llCurrencuUnit)
    LinearLayout llCurrencuUnit;
    @BindView(R.id.llManageWallets)
    LinearLayout llManageWallets;
    @BindView(R.id.llAgreement)
    LinearLayout llAgreement;
    @BindView(R.id.llHepl)
    LinearLayout llHepl;
    @BindView(R.id.llCommunity)
    LinearLayout llCommunity;
    @BindView(R.id.llAboutWinQ)
    LinearLayout llAboutWinQ;
    @BindView(R.id.currentCurrency)
    TextView currentCurrency;
    @BindView(R.id.version)
    TextView version;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        if (isSupportFingerPrint()) {
            switchBar.setChecked(SpUtil.getBoolean(getActivity(), ConstantValue.fingerprintUnLock, true));
        } else {
            llTouchId.setVisibility(View.GONE);
        }
        switchBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.putBoolean(getActivity(), ConstantValue.fingerprintUnLock, isChecked);
            }
        });
        currentCurrency.setText(ConstantValue.currencyBean.getName());
        version.setText("Version：" + VersionUtil.getAppVersionName(getActivity()));
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerSettingsComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .settingsModule(new SettingsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SettingsContract.SettingsContractPresenter presenter) {
        mPresenter = (SettingsPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            currentCurrency.setText(ConstantValue.currencyBean.getName());
        } else if (requestCode == 3) {
            EventBus.getDefault().post(new ChangeWallet());
        }
    }

    @OnClick({R.id.llTouchId, R.id.llPassWord, R.id.llCurrencuUnit, R.id.llManageWallets, R.id.llAgreement, R.id.llHepl, R.id.llCommunity, R.id.llAboutWinQ})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llTouchId:

                break;
            case R.id.llPassWord:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.llCurrencuUnit:
                startActivityForResult(new Intent(getActivity(), CurrencyUnitActivity.class), 2);
                break;
            case R.id.llManageWallets:
                startActivityForResult(new Intent(getActivity(), ChooseWalletActivity.class), 3);
                break;
            case R.id.llAgreement:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "https://winq.net/disclaimer.html");
                intent.putExtra("title", "Service agreement");
                startActivity(intent);
                break;
            case R.id.llHepl:

                break;
            case R.id.llCommunity:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url1 = Uri.parse("https://winq.net");
                intent1.setData(content_url1);
                startActivity(intent1);
                break;
            case R.id.llAboutWinQ:
                startActivity(new Intent(getActivity(), TestActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否支持指纹解锁
     *
     * @return
     */
    private boolean isSupportFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) AppConfig.getInstance().getSystemService(Context.FINGERPRINT_SERVICE);
                if (fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                    return true;
                }
            } catch (Exception e) {

            }
        }
        return false;
    }
}