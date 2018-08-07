package com.stratagile.qlink.ui.activity.vpn;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.eventbus.VpnSendEnd;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerRegisteWindowVpnActivityComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteWindowVpnActivityContract;
import com.stratagile.qlink.ui.activity.vpn.module.RegisteWindowVpnActivityModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisteWindowVpnActivityPresenter;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/08/03 11:56:07
 */

public class RegisteWindowVpnActivityActivity extends BaseActivity implements RegisteWindowVpnActivityContract.View {

    @Inject
    RegisteWindowVpnActivityPresenter mPresenter;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.paste)
    LinearLayout paste;
    @BindView(R.id.scan)
    LinearLayout scan;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_send)
    Button btSend;

    VpnEntity addVpnEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.add_window_asset);
        ButterKnife.bind(this);
        setTitle(getString(R.string.VPN_DETAIL).toUpperCase());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
        DaggerRegisteWindowVpnActivityComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .registeWindowVpnActivityModule(new RegisteWindowVpnActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegisteWindowVpnActivityContract.RegisteWindowVpnActivityContractPresenter presenter) {
        mPresenter = (RegisteWindowVpnActivityPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.paste, R.id.scan, R.id.bt_back, R.id.bt_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.paste:
                try {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将ClipData内容放到系统剪贴板里。
                    address.setText(cm.getPrimaryClip().getItemAt(0).getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.scan:
                mPresenter.getScanPermission();
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_send:
                String toxid = address.getText().toString();
                if(toxid.equals(""))
                {
                    ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.cannot_register_empty_vpn_asset));
                    return;
                }
                if (qlinkcom.GetP2PConnectionStatus() <= 0) {
                    ToastUtil.displayShortToast(getString(R.string.you_offline));
                    return;
                }
                showProgressDialog();
                int friendNum = qlinkcom.GetFriendNumInFriendlist(toxid);
                if (friendNum < 0 && !toxid.equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                    KLog.i(friendNum + "需要添加window好友");
                    friendNum = qlinkcom.AddFriend(toxid);
                    if (friendNum >= 0) {
                        KLog.i("添加后好友索引" + friendNum);
                    }
                }else {
                    KLog.i(friendNum + "已经是好友");
                }
                String toxidStr = new String(toxid).trim();
                if (qlinkcom.GetFriendConnectionStatus(toxidStr+"") > 0) {
                    KLog.i(friendNum + "在线");
                    addVpnEntity = new VpnEntity();
                    addVpnEntity.setP2pIdPc(toxid);
                    addVpnEntity.setP2pId(SpUtil.getString(this, ConstantValue.P2PID, ""));
                    addVpnEntity.setFriendNum(toxidStr);
                    mPresenter.preAddVpn(addVpnEntity);
                } else {
                    KLog.i(friendNum + "离线");
                    ToastUtil.displayShortToast(getString(R.string.The_friend_is_not_online));
                    closeProgressDialog();
                   /* Intent intent = new Intent(this, RegisteVpnActivity.class);
                    intent.putExtra("flag", "");
                    startActivityForResult(intent, 0);
                    this.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);*/
                }
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConfigFile(VpnSendEnd vpnSendEnd) {
        KLog.i("c层的vpn配置文件传输完毕了，");
        if(vpnSendEnd.getProfileLocalPath() == null || "".equals(vpnSendEnd.getProfileLocalPath()))
        {
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.configuration_profile_error));
            return;
        }
        closeProgressDialog();
        Intent intent = new Intent(this, RegisteVpnActivity.class);
        intent.putExtra("flag", "");
        addVpnEntity.setProfileLocalPath(vpnSendEnd.getProfileLocalPath());
        intent.putExtra("vpnentity", addVpnEntity);
        startActivityForResult(intent, 0);
        this.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
        //mPresenter.vpnProfileSendComplete();
    }

    @Override
    public void getScanPermissionSuccess() {
        Intent intent1 = new Intent(this, ScanQrCodeActivity.class);
        startActivityForResult(intent1, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (address != null) {
                address.setText(data.getStringExtra("result"));
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}