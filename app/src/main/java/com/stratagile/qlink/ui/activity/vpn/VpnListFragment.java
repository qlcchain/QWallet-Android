package com.stratagile.qlink.ui.activity.vpn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.activities.DisconnectVPN;
import com.stratagile.qlink.api.ExternalAppDatabase;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.core.ConnectionStatus;
import com.stratagile.qlink.core.IServiceStatus;
import com.stratagile.qlink.core.OpenVPNStatusService;
import com.stratagile.qlink.core.PasswordCache;
import com.stratagile.qlink.core.Preferences;
import com.stratagile.qlink.core.ProfileManager;
import com.stratagile.qlink.core.VPNLaunchHelper;
import com.stratagile.qlink.core.VpnStatus;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
import com.stratagile.qlink.entity.eventbus.DisconnectVpn;
import com.stratagile.qlink.entity.eventbus.FreeCount;
import com.stratagile.qlink.entity.eventbus.SelectCountry;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.entity.eventbus.VpnRegisterSuccess;
import com.stratagile.qlink.entity.eventbus.VpnSendEnd;
import com.stratagile.qlink.entity.eventbus.VpnTitle;
import com.stratagile.qlink.entity.im.Message;
import com.stratagile.qlink.entity.vpn.VpnPrivateKeyRsp;
import com.stratagile.qlink.entity.vpn.VpnUserAndPasswordRsp;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.VpnListComponent;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.service.ClientVpnService;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.activity.im.ConversationActivity;
import com.stratagile.qlink.ui.activity.seize.SeizeActivity;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerVpnListComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.VpnListContract;
import com.stratagile.qlink.ui.activity.vpn.module.VpnListModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.VpnListPresenter;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.VpnSpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.vpn.VpnListAdapter;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.VersionUtil;
import com.stratagile.qlink.utils.VpnUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static com.stratagile.qlink.LaunchVPN.CLEARLOG;
import static com.stratagile.qlink.LaunchVPN.START_VPN_PROFILE;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/02/06 15:16:44
 */

public class VpnListFragment extends MyBaseFragment implements VpnListContract.View {

    @Inject
    VpnListPresenter mPresenter;

    @Inject
    VpnListAdapter vpnListAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    int freeNum = 0;

    private String country;

    private Boolean isReport = false;
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vpn_list, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        recyclerView.addItemDecoration(new VpnSpaceItemDecoration((int) getResources().getDimension(R.dimen.x20)));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setAdapter(vpnListAdapter);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVpn();
            }
        });
        vpnListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!vpnListAdapter.getItem(position).isOnline()) {
                    //vpn资产不在线
                } else {
                    if (!vpnListAdapter.getItem(position).getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                        return;
                    }
                    if (SpUtil.getString(getContext(), ConstantValue.walletPassWord, "").equals("") && SpUtil.getString(getContext(), ConstantValue.fingerPassWord, "").equals("")) {
                        Intent intent = new Intent(getActivity(), CreateWalletPasswordActivity.class);
                        intent.putExtra("position", position + "");
                        startActivityForResult(intent, 3);
                        return;
                    }
                    if (ConstantValue.isShouldShowVertifyPassword) {
                        Intent intent = new Intent(getActivity(), VerifyWalletPasswordActivity.class);
                        intent.putExtra("position", position + "");
                        startActivityForResult(intent, 3);
                        return;
                    }
                    List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                    if (walletList == null || walletList.size() == 0) {
                        Intent intent = new Intent(getActivity(), NoWalletActivity.class);
                        intent.putExtra("flag", "nowallet");
                        intent.putExtra("position", position + "");
                        startActivityForResult(intent, 3);
                        return;
                    }
                    if (vpnListAdapter.getItem(position).getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                        //自己注册的vpn资产
                        Intent intent = new Intent(getActivity(), RegisteVpnActivity.class);
                        KLog.i(vpnListAdapter.getData().get(position).toString());
                        intent.putExtra("flag", "update");
                        intent.putExtra("vpnentity", vpnListAdapter.getData().get(position));
                        startActivityForResult(intent, 0);
                        getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                    } else {
                        //别人注册的vpn资产，并且在线
                        if (vpnListAdapter.getItem(position).getIsConnected()) {
                            Intent intent = new Intent(getActivity(), ConnectVpnSuccessActivity.class);
                            KLog.i(vpnListAdapter.getData().get(position).toString());
                            intent.putExtra("vpnentity", vpnListAdapter.getData().get(position));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(getActivity(), ConnectVpnActivity.class);
                            KLog.i(vpnListAdapter.getData().get(position).toString());
                            intent.putExtra("vpnentity", vpnListAdapter.getData().get(position));
                            startActivityForResult(intent, 1);
                        }
                    }
                }
            }
        });
        vpnListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.freind_avater:
                        if (vpnListAdapter.getItem(position).isOnline()) {
                            //ToastUtil.displayShortToast(getString(R.string.Please_create_a_wallet_to_continue));
                            Intent intent = new Intent(getActivity(), ConversationActivity.class);
                            intent.putExtra("vpnEntity", vpnListAdapter.getItem(position));
                            intent.putExtra("assetType", "3");
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        vpnListAdapter.setOnVpnOpreateListener(new VpnListAdapter.OnVpnOpreateListener() {
            @Override
            public void onConnect(VpnEntity vpnEntity) {
                //vpn已经连接，需要先断开已经连接的vpn
                if (VpnStatus.isVPNActive() && vpnListAdapter.getData().get(0).isConnected()) {
                    showChangeVpnDialog(vpnEntity);
                    return;
                }
                //连接自己的vpn
                if (vpnEntity.getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                    getActivity().startService(new Intent(getActivity(), ClientVpnService.class));
                } else {

                }
                //将私钥，密码都置为默认值
                mTransientCertOrPCKS12PW = null;
                mTransientAuthPW = null;

                mPresenter.preConnectVpn(vpnEntity);
                connectVpnEntity = vpnEntity;
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            }

            @Override
            public void onDisConnect() {
                showDisConnectVpnDialog(connectVpnEntity);
            }
        });
        return view;
    }

    /**
     * 显示vpn注册时需要扣费的dialog
     */
    @SuppressLint("StringFormatMatches")
    @Override
    public void showNeedQlcDialog(VpnEntity vpnEntity) {
        if (ConstantValue.freeNum != 0) {
            showFreeQlcDialog(vpnEntity);
            return;
        }
        if (SpUtil.getString(getContext(), ConstantValue.walletPassWord, "").equals("") && SpUtil.getString(getContext(), ConstantValue.fingerPassWord, "").equals("")) {
            Intent intent = new Intent(getActivity(), CreateWalletPasswordActivity.class);
            startActivity(intent);
            vpnListAdapter.notifyDataSetChanged();
            return;
        }
        if (ConstantValue.isShouldShowVertifyPassword) {
            Intent intent = new Intent(getActivity(), VerifyWalletPasswordActivity.class);
            startActivity(intent);
            vpnListAdapter.notifyDataSetChanged();
            return;
        }
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList == null || walletList.size() == 0) {
            Intent intent = new Intent(getActivity(), NoWalletActivity.class);
            intent.putExtra("flag", "nowallet");
            startActivity(intent);
            vpnListAdapter.notifyDataSetChanged();
            return;
        }
        if (mBalance != null) {
            if (Float.parseFloat(mBalance.getData().getQLC() + "") < vpnEntity.getQlc() || mBalance.getData().getGAS() < 0.0001) {
                ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
                return;
            }
        } else {
            getbalance(new ChangeWallet());
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_need_qlc_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(Html.fromHtml(AppConfig.getInstance().getResources().getString(R.string.Just_cost_QLC_Connect_NOW, ("" + vpnEntity.getQlc()))));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                vpnListAdapter.notifyDataSetChanged();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPresenter.dialogConfirm();
            }
        });
        dialog.show();
    }

    /**
     * 显示vpn注册时需要扣费的dialog
     */
    public void showFreeQlcDialog(VpnEntity vpnEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_need_qlc_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.This_will_consume_one_of_your_free_connections));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                vpnListAdapter.notifyDataSetChanged();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mPresenter.checkSharerConnect();
            }
        });
        dialog.show();
    }

    Balance mBalance;

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getbalance(ChangeWallet changeWallet) {
        if (AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().size() != 0) {
            Map<String, String> map = new HashMap<>();
            map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getContext(), ConstantValue.currentWallet, 0)).getAddress());
            mPresenter.getWalletBalance(map);
        }
    }

    @Override
    public void onGetBalancelSuccess(Balance balance) {
        mBalance = balance;
    }

    @Override
    public void refreshList() {
        vpnListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetFreeNumBack(int num) {
        ConstantValue.freeNum = num;
        EventBus.getDefault().post(new FreeCount(num));
    }

    /**
     * 断开vpn的时候显示的dialog
     */
    public void showDisConnectVpnDialog(VpnEntity vpnEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_disconnect_vpn_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.Do_you_want_to_disconnect));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                vpnListAdapter.notifyDataSetChanged();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(BroadCastAction.disconnectVpn);
                getActivity().sendBroadcast(intent);
                if (connectVpnEntity != null) {
                    connectVpnEntity.setIsConnected(false);
                }
                AppConfig.currentUseVpn = null;
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(connectVpnEntity);
            }
        });
        dialog.show();
    }

    /**
     * 切换vpn的时候显示的dialog
     */
    public void showChangeVpnDialog(VpnEntity vpnEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_change_vpn_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.Please_disconnect_the_current_one_first));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                vpnListAdapter.notifyDataSetChanged();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(BroadCastAction.disconnectVpn);
                getActivity().sendBroadcast(intent);

//                mPresenter.preConnectVpn(vpnEntity);
//                connectVpnEntity = vpnEntity;
//                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            }
        });
        dialog.show();
    }


    VpnEntity connectVpnEntity;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeToTestWallet(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
        getVpn();
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        country = ConstantValue.longcountry.equals("") ? "Others" : ConstantValue.longcountry;
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.stratagile.qlink.VPN_STATUS");
        getActivity().registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    public void fetchData() {
        refreshLayout.setRefreshing(true);
        getVpn();
        getbalance(new ChangeWallet());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerVpnListComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .vpnListModule(new VpnListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(VpnListContract.VpnListContractPresenter presenter) {
        mPresenter = (VpnListPresenter) presenter;
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
    public void setVpnList(ArrayList<VpnEntity> vpnList) {
        refreshLayout.setRefreshing(false);
        Collections.sort(vpnList);
        vpnListAdapter.setNewData(vpnList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateVpnList(ArrayList<VpnEntity> vpnList) {
        KLog.i("更新vpn列表");
        if (vpnList == null || vpnList.size() == 0) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            List<VpnEntity> showList = vpnListAdapter.getData();
            if (showList == null || showList.size() == 0) {
                return;
            }
            for (int i = 0; i < showList.size(); i++) {
                for (VpnEntity vpnEntity1 : vpnEntityList) {
                    if (showList.get(i).getVpnName().equals(vpnEntity1.getVpnName()) && VpnUtil.isInSameNet(vpnEntity1)) {
                        vpnEntity1.setUnReadMessageCount(showList.get(i).getUnReadMessageCount());
                        showList.set(i, vpnEntity1);
                    }
                }
            }
            Collections.sort(showList);
            vpnListAdapter.setNewData(showList);
            return;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateVpnList(DisconnectVpn disconnectVpn) {
        KLog.i("收到 DisconnectVpn");
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        List<VpnEntity> showList = vpnListAdapter.getData();
        if (showList == null || showList.size() == 0) {
            return;
        }
        for (int i = 0; i < showList.size(); i++) {
            if (showList.get(i).getIsConnected()) {
                KLog.i("设置" + showList.get(i).getVpnName() + "为未连接。。。");
                showList.get(i).setIsConnected(false);
            }
            for (VpnEntity vpnEntity1 : vpnEntityList) {
                if (showList.get(i).getVpnName().equals(vpnEntity1.getVpnName()) && VpnUtil.isInSameNet(vpnEntity1)) {
                    vpnEntity1.setUnReadMessageCount(showList.get(i).getUnReadMessageCount());
                    showList.set(i, vpnEntity1);
                }
            }
        }
        Collections.sort(showList);
        vpnListAdapter.setNewData(showList);
    }

    /**
     * @param vpnRegisterSuccess
     * @see RegisteVpnActivity#registVpnSuccess
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateVpnListFromServer(VpnRegisterSuccess vpnRegisterSuccess) {
        getVpn();
    }

    /**
     * 收到聊天消息，加上未读提醒
     *
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivedMessage(Message message) {
        if (message.getAssetType() == 3) {
            for (int i = 0; i < vpnListAdapter.getData().size(); i++) {
                if (vpnListAdapter.getItem(i).getVpnName().equals(message.getAssetName())) {
                    vpnListAdapter.getItem(i).setUnReadMessageCount(1);
                    if (vpnListAdapter.getItem(i).getGroupNum() == -1) {
                        vpnListAdapter.getItem(i).setGroupNum(message.getGroupNum());
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnListAdapter.getItem(i));
                    }
                    vpnListAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showGuideVpnList(ShowGuide showGuide) {
        if (showGuide.getNumber() == 1) {
            showGuideViewVpnList();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clearVpnMessageCount(VpnEntity vpnEntity) {
        for (int i = 0; i < vpnListAdapter.getData().size(); i++) {
            if (vpnListAdapter.getItem(i).getVpnName().equals(vpnEntity.getVpnName())) {
                vpnListAdapter.getItem(i).setUnReadMessageCount(0);
                vpnListAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getVpnFromSelectCountry(ContinentAndCountry.ContinentBean.CountryBean countryBean) {
        Map<String, String> map = new HashMap<>();
        map.put("country", countryBean.getName());
        country = countryBean.getName();
        mPresenter.getVpn(map);
        refreshLayout.setRefreshing(true);
    }

    private void getVpn() {
        if (VpnStatus.isVPNActive()) {

        } else {
            for (int i = 0; i < vpnListAdapter.getItemCount(); i++) {
                if (vpnListAdapter.getItem(i).isConnected()) {
                    vpnListAdapter.getItem(i).setIsConnected(false);
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnListAdapter.getItem(i));
                    break;
                }
            }
        }
        ArrayList<String> p2pIdList = new ArrayList<>();
        p2pIdList.add(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
        for (int i = 0; i < vpnListAdapter.getItemCount(); i++) {
            if (!vpnListAdapter.getItem(i).isOnline()) {
                if (p2pIdList.contains(vpnListAdapter.getItem(i).getP2pId())) {

                } else {
                    //
                    if (qlinkcom.GetP2PConnectionStatus() > 0) {
                        qlinkcom.AddFriend(vpnListAdapter.getItem(i).getP2pId());
                        KLog.i("重新添加好友 " + vpnListAdapter.getItem(i).getP2pId());
                        LogUtil.addLog("重新添加好友 " + vpnListAdapter.getItem(i).getP2pId(), getClass().getSimpleName());
                        p2pIdList.add(vpnListAdapter.getItem(i).getP2pId());
                    }
                }
            }
        }
        Map<String, String> map = new HashMap<>();
        KLog.i("国家为：" + country);
        map.put("country", country);
        mPresenter.getVpn(map);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void showGuideViewVpnList() {
        if (!GuideSpUtil.getBoolean(getActivity(), GuideConstantValue.isShowVpnListGuide, false)) {
            GuideSpUtil.putBoolean(getActivity(), GuideConstantValue.isShowVpnListGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(recyclerView.getChildAt(0))
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPaddingLeft((int) getResources().getDimension(R.dimen.x15))
                    .setHighTargetPaddingRight((int) getResources().getDimension(R.dimen.x15))
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new VpnListComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(getActivity());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConfigFile(VpnSendEnd vpnSendEnd) {
        KLog.i("c层的vpn配置文件传输完毕了，");
        if(ConstantValue.isWindows)//注册电脑资产
        {
            return;
        }
        mPresenter.vpnProfileSendComplete();
    }

    /**
     * @param vpnPrivateKeyRsp
     * @see Qsdk# handleVpnPrivateKeyRsp(int, VpnPrivateKeyRsp)
     * 拿到私钥的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void privateKeyBack(VpnPrivateKeyRsp vpnPrivateKeyRsp) {
        if (vpnPrivateKeyRsp.getPrivateKey() == null || vpnPrivateKeyRsp.getPrivateKey().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Private_Key_Error));
            KLog.i("error");
            closeProgressDialog();
            if (connectVpnEntity != null && !isReport) {
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                Map<String, Object> map = new HashMap<>();
                map.put("vpnName", connectVpnEntity.getVpnName());
                map.put("status", 0);
                map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " Private Key Error");
                KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_Private Key Error");
                mPresenter.reportVpnInfo(map);
                isReport = true;
            }
            return;
        }
        KLog.i("收到了私钥的返回了。。。。");
        mPresenter.handleSendMessage(0);
        mTransientCertOrPCKS12PW = vpnPrivateKeyRsp.getPrivateKey();
        mResult.mKeyPassword = vpnPrivateKeyRsp.getPrivateKey();
        KLog.i("私钥验证完成，开始连接，打开OpenVPNStatusService服务。");
        Intent intent = new Intent(getActivity(), OpenVPNStatusService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * @param vpnUserAndPasswordRsp
     * @see Qsdk# handleVpnUserAndPasswordRsp(int, VpnUserAndPasswordRsp)
     * 拿到账号和密码的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userNameAndPasswordBack(VpnUserAndPasswordRsp vpnUserAndPasswordRsp) {
        if (vpnUserAndPasswordRsp.getUserName() == null || vpnUserAndPasswordRsp.getUserName().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.username_or_password_error));
            closeProgressDialog();
            if (connectVpnEntity != null && !isReport) {
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                Map<String, Object> map = new HashMap<>();
                map.put("vpnName", connectVpnEntity.getVpnName());
                map.put("status", 0);
                map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " UserName Or Password Error");
                KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_UserName Or Password Error");
                mPresenter.reportVpnInfo(map);
                isReport = true;
            }
            return;
        }
        KLog.i(vpnUserAndPasswordRsp.toString());
        mPresenter.handleSendMessage(0);
        mResult.mUsername = vpnUserAndPasswordRsp.getUserName();
        mResult.mPassword = vpnUserAndPasswordRsp.getPassword();
        mTransientAuthPW = vpnUserAndPasswordRsp.getPassword();
        KLog.i("用户名和密码验证完成，开始连接，打开OpenVPNStatusService服务。");
        Intent intent = new Intent(getActivity(), OpenVPNStatusService.class);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkSharerConnectRsp(CheckConnectRsp checkConnectRsp) {
        closeProgressDialog();
        //showProgressDialog();
        isReport = false;
        mPresenter.connectShareSuccess();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            KLog.i("服务连接了。。。");
            IServiceStatus service = IServiceStatus.Stub.asInterface(binder);
            try {
                if (mTransientAuthPW != null) {
                    service.setCachedPassword(mResult.getUUIDString(), PasswordCache.AUTHPASSWORD, mTransientAuthPW);
                }
                if (mTransientCertOrPCKS12PW != null) {
                    service.setCachedPassword(mResult.getUUIDString(), PasswordCache.PCKS12ORCERTPASSWORD, mTransientCertOrPCKS12PW);
                }

                onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            getActivity().unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            KLog.i("服务断开连接了。。。。");
        }
    };

    VpnProfile mResult;

    @Override
    public void startOrStopVPN(VpnProfile profile) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mResult = profile;
                if (VpnStatus.isVPNActive() && profile.getUUIDString().equals(VpnStatus.getLastConnectedVPNProfile())) {
                    Intent disconnectVPN = new Intent(getActivity(), DisconnectVPN.class);
                    startActivity(disconnectVPN);
                } else {
                    if (Preferences.getDefaultSharedPreferences(getActivity()).getBoolean(CLEARLOG, true))
                        VpnStatus.clearLog();

                    // we got called to be the starting point, most likely a shortcut
                    String shortcutUUID = profile.getUUID().toString();
                    String shortcutName = profile.getName().toString();

                    VpnProfile profileToConnect = ProfileManager.get(getActivity(), shortcutUUID);
                    if (shortcutName != null && profileToConnect == null) {
                        profileToConnect = ProfileManager.getInstance(getActivity()).getProfileByName(shortcutName);
                        if (!(new ExternalAppDatabase(getActivity()).checkRemoteActionPermission(getActivity()))) {
                            if (connectVpnEntity != null && !isReport) {
                                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                                Map<String, Object> map = new HashMap<>();
                                map.put("vpnName", connectVpnEntity.getVpnName());
                                map.put("status", 0);
                                map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " no Permission");
                                KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_no Permission");
                                mPresenter.reportVpnInfo(map);
                                isReport = true;
                            }
                            return;
                        }
                    }
                    KLog.i("startOrStopVPN  2222");

                    if (profileToConnect == null) {
                        VpnStatus.logError(R.string.shortcut_profile_notfound);
                        KLog.i("startOrStopVPN  3333");
                        // show Log window to display error
//                finish();
                    } else {
//                mSelectedProfile = profileToConnect;
                        launchVPN();
                    }
                }
            }
        }).start();
    }

    /**
     * 密码
     */
    private String mTransientAuthPW;
    /**
     * 私钥
     */
    private String mTransientCertOrPCKS12PW;
    private boolean mhideLog = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            ArrayList<VpnEntity> showList = new ArrayList<>();
            for (int i = 0; i < vpnListAdapter.getData().size(); i++) {
                for (VpnEntity vpnEntity : vpnEntityList) {
                    if (VpnUtil.isInSameNet(vpnEntity) && vpnListAdapter.getItem(i).getVpnName().equals(vpnEntity.getVpnName())) {
                        showList.add(vpnEntity);
                        break;
                    }
                }
            }
            Collections.sort(showList);
            vpnListAdapter.setNewData(showList);
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
//            vpnListAdapter.getViewByPosition(recyclerView, Integer.parseInt(data.getStringExtra("position")), R.id.cardView).performClick();
        }
        if (requestCode == START_VPN_PROFILE) {
            if (connectVpnEntity != null && connectVpnEntity.getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                mResult.mUsername = connectVpnEntity.getUsername();
            }
            KLog.i("onActivityResult的requestCode   为  START_VPN_PROFILE");
            if (resultCode == Activity.RESULT_OK) {
                KLog.i("开始检查配置文件的类型");
                if (connectVpnEntity != null  && connectVpnEntity.getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                    mTransientCertOrPCKS12PW = connectVpnEntity.getPrivateKeyPassword();
                    mResult.mUsername = connectVpnEntity.getUsername();
                    mTransientAuthPW = connectVpnEntity.getPassword();
                    mResult.mPassword = connectVpnEntity.getPassword();
                    mResult.mKeyPassword = connectVpnEntity.getPrivateKeyPassword();
                }
                int needpw = mResult.needUserPWInput(mTransientCertOrPCKS12PW, mTransientAuthPW);
                if (needpw != 0) {
                    VpnStatus.updateStateString("USER_VPN_PASSWORD", "", R.string.state_user_vpn_password,
                            ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
                    if (!connectVpnEntity.getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                        mPresenter.getPasswordFromRemote(needpw);
                    }
                } else {
                    KLog.i("不需要密码，直接连接，，。");
                    ConstantValue.isConnectedVpn = false;
                    SharedPreferences prefs = Preferences.getDefaultSharedPreferences(getActivity());
                    boolean showLogWindow = prefs.getBoolean("showlogwindow", true);

                    if (!mhideLog && showLogWindow)
//                        showLogWindow();
                        ProfileManager.updateLRU(getActivity(), mResult);
                    VPNLaunchHelper.startOpenVpn(mResult, getActivity());
                    startListener();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User does not want us to start, so we just vanish
                VpnStatus.updateStateString("USER_VPN_PERMISSION_CANCELLED", "", R.string.state_user_vpn_permission_cancelled,
                        ConnectionStatus.LEVEL_NOTCONNECTED);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    VpnStatus.logError(R.string.nought_alwayson_warning);
                    if(connectVpnEntity  != null  && !isReport)
                    {
                        SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName()+"_status","0");
                        SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName()+"_lasttime",System.currentTimeMillis() +"");
                        Map<String, Object> map = new HashMap<>();
                        map.put("vpnName",connectVpnEntity.getVpnName() );
                        map.put("status",0 );
                        map.put("mark",VersionUtil.getAppVersionName(getActivity()) + " If you did not get a VPN confirmation dialog" );
                        KLog.i("winqRobot_vpnName:"+ connectVpnEntity.getVpnName()+ "_If you did not get a VPN confirmation dialog" );
                        mPresenter.reportVpnInfo(map);
                        isReport = true;
                    }
                }

//                finish();
            }
        }
    }

    private FirebaseAnalytics mFirebaseAnalytics;

    private void startListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (vpnDetailstatus != null && vpnDetailstatus.contains("CONNECTED")) {
                    KLog.i("时间到了，已经连接了");
                } else if (vpnDetailstatus != null) {
                    closeProgressDialog();
                    if (ConstantValue.isConnectVpn) {
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectVpnFailedActivity");
                        if (mFirebaseAnalytics != null) {
                            mFirebaseAnalytics.logEvent("ConnectVpnFailedActivity", bundle);
                        }
                        ConstantValue.isConnectVpn = false;
                    }
                    Intent intent = new Intent();
                    intent.setAction(BroadCastAction.disconnectVpn);
                    getActivity().sendBroadcast(intent);
                    if (connectVpnEntity != null && !isReport && !ConstantValue.isConnectedVpn) {
                        KLog.i("error");
                        ToastUtil.displayShortToast(getString(R.string.Connect_to_VPN_error_last_VPN_status) + vpnDetailstatus);
                        SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                        SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                        Map<String, Object> map = new HashMap<>();
                        map.put("vpnName", connectVpnEntity.getVpnName());
                        map.put("status", 0);
                        map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " Connect to VPN error, last VPN status:" + vpnDetailstatus);
                        KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_More than two minutes");
                        mPresenter.reportVpnInfo(map);
                        isReport = true;
                    }
                } else {
                    closeProgressDialog();
                    if (ConstantValue.isConnectVpn) {
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectVpnFailedActivity");
                        if (mFirebaseAnalytics != null) {
                            mFirebaseAnalytics.logEvent("ConnectVpnFailedActivity", bundle);
                        }
                        ConstantValue.isConnectVpn = false;
                    }
                    if (connectVpnEntity != null && !isReport) {
                        SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                        SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                        Map<String, Object> map = new HashMap<>();
                        map.put("vpnName", connectVpnEntity.getVpnName());
                        map.put("status", 0);
                        map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " Connect to VPN error");
                        KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_Connect to VPN error");
                        mPresenter.reportVpnInfo(map);
                        isReport = true;
                    }
                    KLog.i("error");
                    ToastUtil.displayShortToast(getString(R.string.Connect_to_VPN_error));
                    Intent intent = new Intent();
                    intent.setAction(BroadCastAction.disconnectVpn);
                    getActivity().sendBroadcast(intent);
                }
            }
        }).start();
    }

    void launchVPN() {
        KLog.i("开启vpn");
        int vpnok = mResult.checkProfile(getActivity());
        if (vpnok != R.string.no_error_found) {
//            showConfigErrorDialog(vpnok);
            KLog.i("连接vpn出现错误");
            return;
        }

        KLog.i("校验配置文件没有出现错误");

        Intent intent = VpnService.prepare(getActivity());
        // Check if we want to fix /dev/tun
        SharedPreferences prefs = Preferences.getDefaultSharedPreferences(getActivity());
        boolean usecm9fix = prefs.getBoolean("useCM9Fix", false);
        boolean loadTunModule = prefs.getBoolean("loadTunModule", false);

//        if (loadTunModule)
//            execeuteSUcmd("insmod /system/lib/modules/tun.ko");
//
//        if (usecm9fix && !mCmfixed) {
//            execeuteSUcmd("chown system /dev/tun");
//        }

        if (intent != null) {
            VpnStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                    ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
            // Start the query
            try {
                KLog.i("开启vpn的服务");
                startActivityForResult(intent, START_VPN_PROFILE);
            } catch (ActivityNotFoundException ane) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                VpnStatus.logError(R.string.no_vpn_support_image);
//                showLogWindow();
            }
        } else {
            KLog.i("开启vpn的intent为空");
            onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
        }
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    String vpnDetailstatus;

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (connectVpnEntity == null) {
                return;
            }
            KLog.i(intent.getStringExtra("detailstatus"));
            vpnDetailstatus = intent.getStringExtra("detailstatus");
            //if (ConstantValue.showLog) {
            LogUtil.addLog("连接已经注册的vpn的log：" + intent.getStringExtra("detailstatus"), getClass().getSimpleName());
            KLog.i("收到广播了。。。。");
            if (intent.getStringExtra("detailstatus").contains("CONNECTED") && connectVpnEntity != null) {
                LogUtil.addLog("vpn连接成功，开始进行扣费：" + connectVpnEntity.getVpnName(), getClass().getSimpleName());
                if (connectVpnEntity.getProfileLocalPath() != null) {
                    String newPath = Environment.getExternalStorageDirectory() + "/Qlink/vpn";
                    String fileName = "";
                    if (connectVpnEntity.getProfileLocalPath().contains("/")) {
                        fileName = connectVpnEntity.getProfileLocalPath().substring(connectVpnEntity.getProfileLocalPath().lastIndexOf("/"), connectVpnEntity.getProfileLocalPath().length());
                    } else {
                        fileName = "/" + connectVpnEntity.getProfileLocalPath();
                    }
                    File configFile = new File(newPath + fileName);
                    if (configFile.exists()) {
                        configFile.delete();
                    }
                }
                if (mResult != null) {
                    ProfileManager.getInstance(getActivity()).removeProfile(getActivity(), mResult);
                }
                ConstantValue.isConnectedVpn = true;
                connectVpnEntity.setIsConnected(true);
                if (ConstantValue.isConnectVpn) {
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectVpnSuccessActivity");
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectVpnSuccessActivity");
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectVpnSuccessActivity");
                    if (mFirebaseAnalytics != null) {
                        mFirebaseAnalytics.logEvent("ConnectVpnSuccessActivity", bundle);
                    }
                    ConstantValue.isConnectVpn = false;
                }
                openNewThreed();
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(connectVpnEntity);
                for (VpnEntity vpnEntity : vpnListAdapter.getData()) {
                    if (vpnEntity.getVpnName().equals(connectVpnEntity.getVpnName())) {
                        vpnEntity.setIsConnected(true);
                    }
                }
                vpnListAdapter.notifyDataSetChanged();
                closeProgressDialog();
//                onRecordSuccess(true);
            }
        }

        private void openNewThreed() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (connectVpnEntity != null && !isReport) {
                                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                                Map<String, Object> map = new HashMap<>();
                                map.put("vpnName", connectVpnEntity.getVpnName());
                                map.put("status", 1);
                                map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " connect success");
                                KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_no Permission");
                                mPresenter.reportVpnInfo(map);
                                isReport = true;
                            }
                        }
                    });
                }
            }).start();
        }

    }

    @Override
    public void preConnect() {
        ConstantValue.isConnectVpn = true;
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "beginConnectVpn");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "beginConnectVpn");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "beginConnectVpn");
        mFirebaseAnalytics.logEvent("beginConnectVpn", bundle);
        String currentTime = StringUitl.getNowDateShort();
        String currentTimeFlag = SpUtil.getString(AppConfig.getInstance(), currentTime + "_vpn", "0");
        if (currentTimeFlag.equals("0")) {
            bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "useVpnTotal");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "useVpnTotal");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "useVpnTotal");
            mFirebaseAnalytics.logEvent("useVpnTotal", bundle);
            SpUtil.putString(AppConfig.getInstance(), currentTime + "_vpn", "1");
        }
        getActivity().startService(new Intent(getActivity(), ClientVpnService.class));
        AppConfig.currentVpnUseType = 1;
        AppConfig.currentUseVpn = connectVpnEntity;
        KLog.i(AppConfig.currentVpnUseType);
        KLog.i(AppConfig.currentUseVpn.getVpnName());
        KLog.i(connectVpnEntity.getProfileLocalPath());
        if (connectVpnEntity.getProfileLocalPath() == null) {
            ToastUtil.displayShortToast(getResources().getString(R.string.import_content_resolve_error));
            closeProgressDialog();
            if (connectVpnEntity != null && !isReport) {
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_status", "0");
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName() + "_lasttime", System.currentTimeMillis() + "");
                Map<String, Object> map = new HashMap<>();
                map.put("vpnName", connectVpnEntity.getVpnName());
                map.put("status", 0);
                map.put("mark", VersionUtil.getAppVersionName(getActivity()) + " Could not read profile to import");
                KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_Could not read profile to import");
                mPresenter.reportVpnInfo(map);
                isReport = true;
            }
            return;
        }
        File configFile = new File(connectVpnEntity.getProfileLocalPath());
        if (configFile.exists()) {
//            ToastUtil.displayShortToast("文件存在");
            configFile.delete();
//            readFile(configFile);
        } else {
        }
        Qsdk.getInstance().sendVpnFileRequest(connectVpnEntity.getFriendNum(), connectVpnEntity.getProfileLocalPath(), connectVpnEntity.getVpnName());
//        mPresenter.handleSendMessage(1);
    }

}