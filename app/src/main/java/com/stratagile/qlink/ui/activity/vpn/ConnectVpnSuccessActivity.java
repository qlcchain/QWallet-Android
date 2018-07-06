package com.stratagile.qlink.ui.activity.vpn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerConnectVpnSuccessComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.ConnectVpnSuccessContract;
import com.stratagile.qlink.ui.activity.vpn.module.ConnectVpnSuccessModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.ConnectVpnSuccessPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/02/09 10:28:44
 */

public class ConnectVpnSuccessActivity extends BaseActivity implements ConnectVpnSuccessContract.View {

    @Inject
    ConnectVpnSuccessPresenter mPresenter;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.tv_vpn)
    TextView tvVpn;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    private VpnEntity vpnEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_connect_vpn_success);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        vpnEntity = getIntent().getParcelableExtra("vpnentity");
        if (vpnEntity.getIpV4Address() != null) {
            tvVpn.setText(vpnEntity.getVpnName() + " / " + vpnEntity.getIpV4Address());
        } else {
            tvVpn.setText(vpnEntity.getVpnName());
        }
        if (vpnEntity.getAvatar() != null) {
            Glide.with(this)
                    .load(API.BASE_URL + vpnEntity.getAvatar().replace("\\", "/"))
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        } else {
            Glide.with(this)
                    .load(R.mipmap.img_connected_head_portrait)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerConnectVpnSuccessComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .connectVpnSuccessModule(new ConnectVpnSuccessModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ConnectVpnSuccessContract.ConnectVpnSuccessContractPresenter presenter) {
        mPresenter = (ConnectVpnSuccessPresenter) presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.tv_vpn, R.id.ll_root, R.id.iv_avater})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_vpn:
                break;
            case R.id.ll_root:
                finish();
                break;
            case R.id.iv_avater:
                showDisconnectVpnDialog();
                break;
            default:
                break;
        }
    }

    private void showDisconnectVpnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(R.string.cancel_connection_long);
        tvContent.setText(R.string.Are_you_sure_to_disconnect);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setText(getString(R.string.cancel).toLowerCase());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(BroadCastAction.disconnectVpn);
                sendBroadcast(intent);
                finish();
            }
        });
        dialog.show();
    }
}