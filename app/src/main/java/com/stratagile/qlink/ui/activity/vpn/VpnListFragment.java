package com.stratagile.qlink.ui.activity.vpn;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.core.VpnStatus;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.DisconnectVpn;
import com.stratagile.qlink.entity.eventbus.SelectCountry;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.entity.eventbus.VpnRegisterSuccess;
import com.stratagile.qlink.entity.eventbus.VpnTitle;
import com.stratagile.qlink.entity.im.Message;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.VpnListComponent;
import com.stratagile.qlink.qlinkcom;
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
import com.stratagile.qlink.ui.adapter.vpn.VpnListAdapter;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

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

    private String country;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vpn_list, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        recyclerView.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.x20)));
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
                    case R.id.iv_vpn_status:
                        if (ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {
                            return;
                        }
                        if (vpnListAdapter.getItem(position).getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                            //自己的资产不能抢注，不能点击，点击无效
//                            if (vpnListAdapter.getItem(position).getIsConnected()) {
//                                showVpnDisconnectDialog(vpnListAdapter.getItem(position));
//                            } else {
                            vpnListAdapter.getViewByPosition(recyclerView, position, R.id.cardView).performClick();
//                            }
                        } else {
                            //别人的资产可以抢注，不管是否在线
//                            vpnListAdapter.getViewByPosition(recyclerView, position, R.id.cardView).performClick();
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
                            Intent intent = new Intent(getActivity(), SeizeActivity.class);
                            intent.putExtra("vpnEntity", vpnListAdapter.getItem(position));
                            intent.putExtra("seizeType", 0);
                            startActivity(intent);
                        }
                        break;
                    case R.id.freind_avater:
                        if (vpnListAdapter.getItem(position).isOnline()) {
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
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeToTestWallet(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
        getVpn();
    }

    /**
     * 显示vpn注册时需要扣费的dialog
     */
    private void showVpnDisconnectDialog(VpnEntity vpnEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(R.string.Disconnect_VPN);
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
                getActivity().sendBroadcast(intent);
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            ArrayList<VpnEntity> showList = new ArrayList<>();
            for (int i = 0; i < vpnListAdapter.getData().size(); i++) {
                for (VpnEntity vpnEntity : vpnEntityList) {
                    if (vpnListAdapter.getItem(i).getVpnName().equals(vpnEntity.getVpnName())) {
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        country = ConstantValue.longcountry.equals("") ? "United States" : ConstantValue.longcountry;
        EventBus.getDefault().register(this);
    }

    @Override
    public void fetchData() {
        refreshLayout.setRefreshing(true);
        getVpn();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
        KLog.i("收到 updateVpnList");
        if (vpnList == null || vpnList.size() == 0) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            List<VpnEntity> showList = vpnListAdapter.getData();
            if (showList == null || showList.size() == 0) {
                return;
            }
            for (int i = 0; i < showList.size(); i++) {
                for (VpnEntity vpnEntity1 : vpnEntityList) {
                    if (showList.get(i).getVpnName().equals(vpnEntity1.getVpnName())) {
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
                if (showList.get(i).getVpnName().equals(vpnEntity1.getVpnName())) {
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
    public void getVpnFromSelectCountry(SelectCountry selectCountry) {
        Map<String, String> map = new HashMap<>();
        map.put("country", selectCountry.getCountry());
        country = selectCountry.getCountry();
        mPresenter.getVpn(map);
        refreshLayout.setRefreshing(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setVpnTitle(VpnTitle vpnTitle) {
        country = vpnTitle.getTitle();
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
        KLog.i(country);
        EventBus.getDefault().post(new VpnTitle(country));
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
}