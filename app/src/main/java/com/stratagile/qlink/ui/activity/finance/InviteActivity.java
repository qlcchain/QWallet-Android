package com.stratagile.qlink.ui.activity.finance;

import android.os.Bundle;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.ui.activity.finance.component.DaggerInviteComponent;
import com.stratagile.qlink.ui.activity.finance.contract.InviteContract;
import com.stratagile.qlink.ui.activity.finance.module.InviteModule;
import com.stratagile.qlink.ui.activity.finance.presenter.InvitePresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.RSAEncrypt;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: $description
 * @date 2019/04/23 15:34:34
 */

public class InviteActivity extends BaseActivity implements InviteContract.View {

    @Inject
    InvitePresenter mPresenter;
    @BindView(R.id.tvIniviteCode)
    TextView tvIniviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("Share with Friends");
        tvIniviteCode.setText(ConstantValue.currentUser.getInviteCode());
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("account", ConstantValue.currentUser.getAccount());
        infoMap.put("token", AccountUtil.getUserToken());
        mPresenter.getInivteRank(infoMap);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerInviteComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .inviteModule(new InviteModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(InviteContract.InviteContractPresenter presenter) {
        mPresenter = (InvitePresenter) presenter;
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
    public void setData(InviteList inviteList) {

    }

}