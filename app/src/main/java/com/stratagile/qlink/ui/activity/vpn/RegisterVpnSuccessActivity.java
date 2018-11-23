package com.stratagile.qlink.ui.activity.vpn;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.eventbus.ChangeViewpager;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerRegisterVpnSuccessComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisterVpnSuccessContract;
import com.stratagile.qlink.ui.activity.vpn.module.RegisterVpnSuccessModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisterVpnSuccessPresenter;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/02/11 16:34:20
 */

public class RegisterVpnSuccessActivity extends BaseActivity implements RegisterVpnSuccessContract.View {

    @Inject
    RegisterVpnSuccessPresenter mPresenter;
    @BindView(R.id.tv_vpn)
    TextView tvVpn;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_continue)
    Button btContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register_vpn_success);
        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        VpnEntity vpnEntity = getIntent().getParcelableExtra("vpnentity");
        tvVpn.setText(vpnEntity.getVpnName());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerRegisterVpnSuccessComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .registerVpnSuccessModule(new RegisterVpnSuccessModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegisterVpnSuccessContract.RegisterVpnSuccessContractPresenter presenter) {
        mPresenter = (RegisterVpnSuccessPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_back, R.id.bt_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                EventBus.getDefault().post(new ChangeViewpager(2));
                finish();
                break;
            case R.id.bt_continue:
                EventBus.getDefault().post(new ChangeViewpager(1));
                finish();
                break;
            default:
                break;
        }
    }
}