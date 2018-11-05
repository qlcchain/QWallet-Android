package com.stratagile.qlink.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kyleduo.switchbutton.SwitchButton;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.ui.activity.main.component.DaggerSettingsComponent;
import com.stratagile.qlink.ui.activity.main.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.main.module.SettingsModule;
import com.stratagile.qlink.ui.activity.main.presenter.SettingsPresenter;
import com.stratagile.qlink.ui.activity.setting.CurrencyUnitActivity;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
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

    @OnClick({R.id.llTouchId, R.id.llPassWord, R.id.llCurrencuUnit, R.id.llManageWallets, R.id.llAgreement, R.id.llHepl, R.id.llCommunity, R.id.llAboutWinQ})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llTouchId:
                break;
            case R.id.llPassWord:
                break;
            case R.id.llCurrencuUnit:
                startActivity(new Intent(getActivity(), CurrencyUnitActivity.class));
                break;
            case R.id.llManageWallets:
                break;
            case R.id.llAgreement:
                break;
            case R.id.llHepl:
                break;
            case R.id.llCommunity:
                break;
            case R.id.llAboutWinQ:
                break;
            default:
                break;
        }
    }
}