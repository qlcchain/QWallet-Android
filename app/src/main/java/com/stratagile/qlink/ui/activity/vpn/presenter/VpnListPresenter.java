package com.stratagile.qlink.ui.activity.vpn.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.activities.DisconnectVPN;
import com.stratagile.qlink.api.ExternalAppDatabase;
import com.stratagile.qlink.api.HttpObserver;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.core.ConfigParser;
import com.stratagile.qlink.core.ConnectionStatus;
import com.stratagile.qlink.core.Preferences;
import com.stratagile.qlink.core.ProfileManager;
import com.stratagile.qlink.core.VPNLaunchHelper;
import com.stratagile.qlink.core.VpnStatus;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.entity.eventbus.DisconnectVpn;
import com.stratagile.qlink.fragments.Utils;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.service.ClientVpnService;
import com.stratagile.qlink.ui.activity.vpn.VpnListFragment;
import com.stratagile.qlink.ui.activity.vpn.contract.VpnListContract;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.VersionUtil;
import com.stratagile.qlink.utils.VpnUtil;
import com.stratagile.qlink.views.FileSelectLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.stratagile.qlink.LaunchVPN.CLEARLOG;
import static com.stratagile.qlink.LaunchVPN.START_VPN_PROFILE;
import static com.stratagile.qlink.utils.VpnUtil.isInSameNet;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: presenter of VpnListFragment
 * @date 2018/02/06 15:16:44
 */
public class VpnListPresenter implements VpnListContract.VpnListContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final VpnListContract.View mView;
    //    private CompositeDisposable mCompositeDisposable;
    private VpnListFragment mFragment;
    private VpnEntity connectVpnEntity;
    int flag = 0;

    private VpnProfile mResult;
    private String mEmbeddedPwFile;
    private String mAliasName = null;
    private transient List<String> mPathsegments;
    private Map<Utils.FileType, FileSelectLayout> fileSelectMap = new HashMap<>();

    @Inject
    public VpnListPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull VpnListContract.View view, VpnListFragment fragment) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
//        mCompositeDisposable = new CompositeDisposable();
        this.mFragment = fragment;
    }

    @Override
    public void subscribe() {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    flag = 0;
                    KLog.i("移除定时监听");
                    removeMessages(1);
                    break;
                case 1:
                    KLog.i(msg.what);
                    flag += 1;
                    if (flag >= 8) {
                        mView.closeProgressDialog();
                        KLog.i("error");
                        flag = 0;
                        Map<String, Object> map = new HashMap<>();
                        map.put("vpnName", connectVpnEntity.getVpnName());
                        map.put("status", 0);
                        map.put("mark", VersionUtil.getAppVersionName(AppConfig.getInstance()) + "  " +  AppConfig.getInstance().getResources().getString(R.string.Connect_to_Sharer_Timeout));
                        KLog.i("winqRobot_vpnName:" + connectVpnEntity.getVpnName() + "_no Permission");
                        reportVpnInfo(map);
                        ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.Connect_to_Sharer_Timeout));
                        mView.refreshList();
                    } else {
                        sendEmptyMessageDelayed(1, 2000);
                    }
                    break;
            }
        }
    };

    @Override
    public void unsubscribe() {
//        if (!mCompositeDisposable.isDisposed()) {
//             mCompositeDisposable.dispose();
//        }
    }

    @Override
    public void getVpn(Map map) {
        if (map.get("country").equals("Others")) {
            httpAPIWrapper.vpnQuery(map).subscribe(new HttpObserver<ChainVpn>() {
                @Override
                public void onNext(ChainVpn chainVpn) {
                    KLog.i("onSuccesse");
                    handleVpnList(chainVpn);
                    onComplete();
                }
            });
        } else {
            httpAPIWrapper.vpnQueryV3(map).subscribe(new HttpObserver<ChainVpn>() {
                @Override
                public void onNext(ChainVpn chainVpn) {
                    KLog.i("onSuccesse");
                    handleVpnList(chainVpn);
                    onComplete();
                }
            });
        }
    }

    private void handleVpnList(ChainVpn chainVpn) {
        ArrayList<ChainVpn.DataBean.VpnListBean> vpnListBeans = new ArrayList<>();
        for (ChainVpn.DataBean dataBean : chainVpn.getData()) {
            vpnListBeans.addAll(dataBean.getVpnList());
        }
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        Iterator itTemp = vpnEntityList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (itTemp.hasNext()) {          //遍历老集合
            VpnEntity obj = (VpnEntity) itTemp.next();       //记录每一个元素
            boolean isHad = false;
            int j = vpnListBeans.size();
            for (int i = 0; i < j; i++) {
                ChainVpn.DataBean.VpnListBean objData = vpnListBeans.get(i);
                if (objData.getVpnName().equals(obj.getVpnName()) && isInSameNet(obj)) {
                    isHad = true;
                    break;
                }
            }
            String P2PID = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "");
            if (!"".equals(P2PID) && !isHad && !P2PID.equals(obj.getP2pId()) && !obj.isConnected()) {
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().delete(obj);
            }
        }
        vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        ArrayList<String> newList = new ArrayList();     //创建新集合
        Iterator it = vpnEntityList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            VpnEntity obj = (VpnEntity) it.next();       //记录每一个元素
            if (!newList.contains(obj.getVpnName()) && isInSameNet(obj)) {      //如果新集合中不包含旧集合中的元素
                newList.add(obj.getVpnName());       //将元素添加
            } else {
                if (isInSameNet(obj)) {
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().delete(obj);
                }
            }
        }
        vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        List<VpnEntity> toAddList = new ArrayList<>();
        String avatar = "";
        /*for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            if(vpnListBean.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "")) && vpnListBean.getImgUrl() != null  && !"".equals(vpnListBean.getImgUrl()))
            {
                avatar = vpnListBean.getImgUrl();
                break;
            }
        }*/
        for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            boolean isAdded = false;
//            LogUtil.addLog("服务器给的资产名为：" + vpnListBean.getVpnName() + "  服务器给的资产p2pid为：" + vpnListBean.getP2pId(), getClass().getSimpleName());
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnListBean.getVpnName().equals(vpnEntity.getVpnName())) {
                    if (!isInSameNet(vpnEntity)) {
//                        isAdded = true;
                        continue;
                    }
                    vpnEntity.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                    vpnEntity.setAssetTranfer(vpnListBean.getQlc());
                    vpnEntity.setAddress(vpnListBean.getAddress());
                    vpnEntity.setP2pId(vpnListBean.getP2pId());
                    vpnEntity.setCountry(vpnListBean.getCountry());
                    vpnEntity.setConnsuccessNum(vpnListBean.getConnsuccessNum());
                    vpnEntity.setOnlineTime(vpnListBean.getOnlineTime());
                    if (vpnListBean.getConnectNum() != 0) {
                        vpnEntity.setQlc((float) vpnListBean.getCost());
                        vpnEntity.setConnectMaxnumber(vpnListBean.getConnectNum());
                        if(vpnEntity.getP2pIdPc() != null && vpnEntity.getP2pIdPc().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "")))
                        {
                            vpnEntity.setAvatar(SpUtil.getString(AppConfig.getInstance(), ConstantValue.myAvatarPath, ""));
                        }else{
                            vpnEntity.setAvatar(vpnListBean.getImgUrl());
                        }
                        vpnEntity.setP2pId(vpnListBean.getP2pId());
                        vpnEntity.setRegisterQlc(vpnListBean.getRegisterQlc());
                        vpnEntity.setProfileLocalPath(vpnListBean.getProfileLocalPath());
                    }
                    if (vpnListBean.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                        if (qlinkcom.GetP2PConnectionStatus() > 0) {
                            vpnEntity.setOnline(true);
                        } else {
                            vpnEntity.setOnline(false);
                        }
                    }
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                VpnEntity tempVpnEntity = new VpnEntity();
                tempVpnEntity.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                tempVpnEntity.setP2pId(vpnListBean.getP2pId());
                tempVpnEntity.setAddress(vpnListBean.getAddress());
                tempVpnEntity.setCountry(vpnListBean.getCountry());
                tempVpnEntity.setAssetTranfer(vpnListBean.getQlc());
                tempVpnEntity.setType(vpnListBean.getType());
                tempVpnEntity.setFriendNum("");
                tempVpnEntity.setGroupNum(-1);
                tempVpnEntity.setQlc(0f);
                tempVpnEntity.setConnsuccessNum(vpnListBean.getConnsuccessNum());
                tempVpnEntity.setOnlineTime(vpnListBean.getOnlineTime());
                tempVpnEntity.setVpnName(vpnListBean.getVpnName());
                tempVpnEntity.setQlc((float) vpnListBean.getCost());
                tempVpnEntity.setConnectMaxnumber(vpnListBean.getConnectNum());
                tempVpnEntity.setAvatar(vpnListBean.getImgUrl());
                tempVpnEntity.setRegisterQlc(vpnListBean.getRegisterQlc());
                tempVpnEntity.setProfileLocalPath(vpnListBean.getProfileLocalPath());
                toAddList.add(tempVpnEntity);
            }
        }
        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insertInTx(toAddList);
        vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (!isInSameNet(vpnEntity)) {
                    continue;
                }
                if (VpnUtil.isInSameNet(vpnEntity) && vpnEntity.getVpnName().equals(vpnListBean.getVpnName()) && "".equals(vpnEntity.getFriendNum())) {
                    //判断是否是好友，是好友就把friendNum添加到WiFientity中，不是好友就要添加好友，再添加。
                    int friendNum = qlinkcom.GetFriendNumInFriendlist(vpnListBean.getP2pId());
                    byte[] p2pId = new byte[100];
                    String friendNumStr = "";
                    qlinkcom.GetFriendP2PPublicKey(vpnListBean.getP2pId(), p2pId);
                    friendNumStr = new String(p2pId).trim();
                    KLog.i(friendNumStr);
                    if (friendNumStr == null) {
                        friendNumStr = "";
                    }
                    if (friendNum < 0 && !vpnEntity.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                        //不是好友，添加
                        KLog.i(friendNum + "需要添加好友" + vpnEntity.getVpnName());
                        friendNum = qlinkcom.AddFriend(vpnListBean.getP2pId());
                        if (friendNum >= 0) {
                            KLog.i(friendNum + "添加后好友" + vpnEntity.getVpnName());
                            vpnEntity.setFriendNum(new String(p2pId).trim());
                        }
                    } else {
                        KLog.i(friendNum + "已经是好友" + vpnEntity.getVpnName());
                        vpnEntity.setFriendNum(friendNumStr);
//                        try {
//                            if (qlinkcom.GetFriendConnectionStatus(vpnEntity.getFriendNum()) > 0 && vpnEntity.getConnectMaxnumber() == 0) {
//                                vpnEntity.setOnline(true);
//                                if (vpnEntity.getProfileLocalPath() == null || "".equals(vpnEntity.getProfileLocalPath())) {
//                                    Map<String, String> infoMap = new HashMap<>();
//                                    infoMap.put("vpnName", vpnEntity.getVpnName());
//                                    infoMap.put("p2pId", vpnEntity.getP2pId());
//                                    LogUtil.addLog("要获取信息的资产信息为：" + vpnEntity.toString(), getClass().getSimpleName());
//                                    QlinkUtil.parseMap2StringAndSend(vpnEntity.getFriendNum(), ConstantValue.vpnBasicInfoReq, infoMap);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                    if (!vpnEntity.getFriendNum().equals("")) {
                        if (qlinkcom.GetFriendConnectionStatus(friendNumStr) > 0) {
                            vpnEntity.setOnline(true);
                        } else {
                            vpnEntity.setOnline(false);
                        }
                    }
                    String myP2pId = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "");
                    if (VpnUtil.isInSameNet(vpnEntity) && vpnEntity.getVpnName().equals(vpnListBean.getVpnName()) && myP2pId.equals(vpnEntity.getP2pId())) {
                        if (ConstantValue.myStatus > 0) {
                            vpnEntity.setOnline(true);
                        }
                    }
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    break;
                }
            }
        }
        LocalAssetsUtils.updateGreanDaoFromLocal();
        preShowVpn(vpnListBeans);
    }

    private void preShowVpn(ArrayList<ChainVpn.DataBean.VpnListBean> vpnListBeans) {
        boolean isAddConnectedVpn = false;
        ArrayList<VpnEntity> showList = new ArrayList<>();
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (ChainVpn.DataBean.VpnListBean vpnListBean : vpnListBeans) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (!isInSameNet(vpnEntity)) {
                    continue;
                }
                if (VpnUtil.isInSameNet(vpnEntity) && vpnEntity.getVpnName().equals(vpnListBean.getVpnName())) {
                    if (vpnEntity.isConnected()) {
                        isAddConnectedVpn = true;
                    }
                    showList.add(vpnEntity);
                    break;
                }
            }
        }
        if (VpnStatus.isVPNActive() && !isAddConnectedVpn) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.isConnected() && VpnUtil.isInSameNet(vpnEntity)) {
                    showList.add(vpnEntity);
                    break;
                }
            }
        }
        mView.setVpnList(showList);
    }

    @Override
    public void preConnectVpn(VpnEntity vpnEntity) {
        connectVpnEntity = vpnEntity;
        //如果是连接自己的vpn，那么直接连接
        String vpnP2pId = vpnEntity.getP2pIdPc() == null ? vpnEntity.getP2pId() : vpnEntity.getP2pIdPc();
        if (vpnEntity.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
            connectVpnForSelf();
            ConstantValue.isConnectedVpn = false;
        } else {
            //连接的是别人的vpn，先显示需要扣费的弹窗
            if(vpnEntity.getP2pIdPc() != null && vpnEntity.getP2pIdPc().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "")))
            {
                checkSharerConnect();
            }else{
                mView.showNeedQlcDialog(vpnEntity);
            }

        }
    }

    @Override
    public void dialogConfirm() {
        Map<String, String> map = new HashMap<>();
        map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0)).getAddress());
        getBalance(map);
    }


    /**
     * 连接的是自己的vpn
     */
    private void connectVpnForSelf() {
        mResult = ProfileManager.get(AppConfig.getInstance(), connectVpnEntity.getProfileUUid());
        if (mResult != null) {
            if (VpnStatus.isVPNActive()) {

            } else {
                mView.startOrStopVPN(mResult);
                mView.showProgressDialog();
            }
        } else {
            KLog.i("profile为空。。。");
            mResult = ProfileManager.getInstance(AppConfig.getInstance()).getProFile(connectVpnEntity.getConfiguration());
            if (mResult != null) {
                connectVpnEntity.setProfileUUid(mResult.getUUIDString());
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(connectVpnEntity);
                mView.startOrStopVPN(mResult);
                mView.showProgressDialog();
            } else {
//                        ToastUtil.displayShortToast("vpn profile is null, please import vpn configuration file agin");
                KLog.i("profile为空。。。");
                if (connectVpnEntity != null && connectVpnEntity.getProfileLocalPath() != null) {
                    File configFile = new File(connectVpnEntity.getProfileLocalPath());
                    if (configFile.exists()) {
                        mView.showProgressDialog();
                        readFile(configFile);
                    } else {
                        ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.error2));
                        mView.closeProgressDialog();
                    }
                }
            }
        }
    }

    @Override
    public void getPasswordFromRemote(int type) {
        KLog.i("从远程拿数据");
        if (type == R.string.password) {
            KLog.i("需要用户名和密码");
            Qsdk.getInstance().sendVpnUserAndPasswordReq(connectVpnEntity.getFriendNum(), connectVpnEntity.getVpnName());
            flag = 0;
            handler.sendEmptyMessage(1);
        } else {
            KLog.i("需要私钥");
            Qsdk.getInstance().sendVpnPrivateKeyReq(connectVpnEntity.getFriendNum(), connectVpnEntity.getVpnName());
            flag = 0;
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    public void handleSendMessage(int message) {
        KLog.i("需要发送消息");
        if (message == 1) {
            KLog.i("开始获取配置文件");
            flag = 0;
        }
        handler.sendEmptyMessage(message);
    }

    @Override
    public void vpnProfileSendComplete() {
        if(connectVpnEntity  == null)
        {
            return;
        }
        String newPath = Environment.getExternalStorageDirectory() + "/Qlink/vpn";
        String fileName = "";
        if (connectVpnEntity.getProfileLocalPath().contains("/")) {
            fileName = connectVpnEntity.getProfileLocalPath().substring(connectVpnEntity.getProfileLocalPath().lastIndexOf("/"), connectVpnEntity.getProfileLocalPath().length());
        } else {
            fileName = "/" + connectVpnEntity.getProfileLocalPath();
        }
        File configFile = new File(newPath + fileName);
        KLog.i("配置文件的hash值为：" + MD5Util.getFileMD5(configFile));
        Uri uri = new Uri.Builder().path(newPath + fileName).scheme("file").build();
        mPathsegments = uri.getPathSegments();
        KLog.i("发送0");
        handler.sendEmptyMessage(0);
        if (configFile.exists()) {
//            ToastUtil.displayShortToast("文件传送过来了");
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.getIsConnected()) {
                    vpnEntity.setIsConnected(false);
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                }
            }
            EventBus.getDefault().post(new DisconnectVpn());
            readFile(configFile);
        } else {
            if(connectVpnEntity  != null)
            {
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName()+"_status","0");
                SpUtil.putString(AppConfig.getInstance(), connectVpnEntity.getVpnName()+"_lasttime",System.currentTimeMillis() +"");
                Map<String, Object> map = new HashMap<>();
                map.put("vpnName",connectVpnEntity.getVpnName() );
                map.put("status",0 );
                map.put("mark","Cannot Read Configuration Profilefile" );
                KLog.i("winqRobot_vpnName:"+ connectVpnEntity.getVpnName()+ "_Cannot Read Configuration Profilefile" );
                reportVpnInfo(map);
            }
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.Cannot_Read_Configuration_Profilefile));
            mView.closeProgressDialog();
        }
    }

    private void getBalance(Map map) {
        httpAPIWrapper.getBalance(map)
                .subscribe(new HttpObserver<Balance>() {
                    @Override
                    public void onNext(Balance balance) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        if (balance.getData().getQLC() > connectVpnEntity.getQlc()) {
                            checkSharerConnect();
                        } else {
                            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.Not_enough_QLC));
                        }
                    }
                });
    }

    /**
     * 检查和分享者的连接情况
     */
    public void checkSharerConnect() {
        if (qlinkcom.GetP2PConnectionStatus() > 0) {
            if (connectVpnEntity.getFriendNum() == null || "".equals(connectVpnEntity.getFriendNum())) {
                byte[] p2pId = new byte[100];
                String friendNumStr = "";
                qlinkcom.GetFriendP2PPublicKey(connectVpnEntity.getP2pId(), p2pId);
                friendNumStr = new String(p2pId).trim();
                KLog.i(friendNumStr);
                if (friendNumStr == null) {
                    friendNumStr = "";
                }
                connectVpnEntity.setFriendNum(friendNumStr);
            }
            if (qlinkcom.GetFriendConnectionStatus(connectVpnEntity.getFriendNum()) > 0) {
                mView.showProgressDialog();
                QlinkUtil.parseMap2StringAndSend(connectVpnEntity.getFriendNum(), ConstantValue.checkConnectReq, new HashMap());
                handler.sendEmptyMessage(1);
            } else {
                ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.The_friend_is_not_online));
            }
        } else {
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.you_offline));
        }
    }

    @Override
    public void connectShareSuccess() {
        KLog.i("移除监听");
        flag = 0;
        if(connectVpnEntity == null)
        {
            return;
        }
        mView.showProgressDialog();
        if (connectVpnEntity.getProfileLocalPath() != null && !connectVpnEntity.getProfileLocalPath().equals("")) {
            mView.preConnect();
        } else {
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.data_damage_of_this_asset));
        }
    }

    private void readFile(File configFile) {
        KLog.i("读取文件,开启子线程");
        ConfigParser cp = new ConfigParser();
        FileInputStream fim = null;
        try {
            fim = new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            InputStreamReader isr = new InputStreamReader(fim);
            InputStream inputStream = new ByteArrayInputStream("".getBytes());
            InputStreamReader isr3 = new InputStreamReader(inputStream);
            cp.parseConfig(isr);
            mResult = cp.convertProfile();
            embedFiles(cp);
            return;

        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
        }
        KLog.i("读取文件到流中成功");
    }

    void embedFiles(ConfigParser cp) {
        KLog.i("embedfiles1111");
        if (mResult.mPKCS12Filename != null) {
            File pkcs12file = findFileRaw(mResult.mPKCS12Filename);
            if (pkcs12file != null) {
                mAliasName = pkcs12file.getName().replace(".p12", "");
            } else {
                mAliasName = "Imported PKCS12";
            }
        }

        KLog.i("embedfiles2222");


        mResult.mCaFilename = embedFile(mResult.mCaFilename, Utils.FileType.CA_CERTIFICATE, false);
        mResult.mClientCertFilename = embedFile(mResult.mClientCertFilename, Utils.FileType.CLIENT_CERTIFICATE, false);
        mResult.mClientKeyFilename = embedFile(mResult.mClientKeyFilename, Utils.FileType.KEYFILE, false);
        mResult.mTLSAuthFilename = embedFile(mResult.mTLSAuthFilename, Utils.FileType.TLS_AUTH_FILE, false);
        mResult.mPKCS12Filename = embedFile(mResult.mPKCS12Filename, Utils.FileType.PKCS12, false);
        mResult.mCrlFilename = embedFile(mResult.mCrlFilename, Utils.FileType.CRL_FILE, true);
        KLog.i("embedfiles3333");
        if (cp != null) {
            mEmbeddedPwFile = cp.getAuthUserPassFile();
            mEmbeddedPwFile = embedFile(cp.getAuthUserPassFile(), Utils.FileType.USERPW_FILE, false);
        }
        KLog.i("embedfiles4444");
        saveProfile();
    }

    private String embedFile(String filename, Utils.FileType type, boolean onlyFindFileAndNullonNotFound) {
        if (filename == null) {
            return null;
        }

        // Already embedded, nothing to do
        if (VpnProfile.isEmbedded(filename)) {
            return filename;
        }

        File possibleFile = findFile(filename, type);
        if (possibleFile == null) {
            if (onlyFindFileAndNullonNotFound) {
                return null;
            } else {
                return filename;
            }
        } else if (onlyFindFileAndNullonNotFound) {
            return possibleFile.getAbsolutePath();
        } else {
            return readFileContent(possibleFile, type == Utils.FileType.PKCS12);
        }

    }

    String readFileContent(File possibleFile, boolean base64encode) {
        byte[] filedata;
        try {
            filedata = readBytesFromFile(possibleFile);
        } catch (IOException e) {
            return null;
        }

        String data;
        if (base64encode) {
            data = Base64.encodeToString(filedata, Base64.DEFAULT);
        } else {
            data = new String(filedata);

        }

        return VpnProfile.DISPLAYNAME_TAG + possibleFile.getName() + VpnProfile.INLINE_TAG + data;

    }

    private byte[] readBytesFromFile(File file) throws IOException {
        InputStream input = new FileInputStream(file);

        long len = file.length();
        if (len > VpnProfile.MAX_EMBED_FILE_SIZE) {
            throw new IOException("File size of file to import too large.");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) len];

        // Read in the bytes
        int offset = 0;
        int bytesRead;
        while (offset < bytes.length
                && (bytesRead = input.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += bytesRead;
        }

        input.close();
        return bytes;
    }

    private File findFile(String filename, Utils.FileType fileType) {
        File foundfile = findFileRaw(filename);

        if (foundfile == null && filename != null && !filename.equals("")) {
        }
        fileSelectMap.put(fileType, null);

        return foundfile;
    }


    private File findFileRaw(String filename) {
        if (filename == null || filename.equals("")) {
            return null;
        }

        // Try diffent path relative to /mnt/sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        File root = new File("/");

        HashSet<File> dirlist = new HashSet<>();

        for (int i = mPathsegments.size() - 1; i >= 0; i--) {
            String path = "";
            for (int j = 0; j <= i; j++) {
                path += "/" + mPathsegments.get(j);
            }
            // Do a little hackish dance for the Android File Importer
            // /document/primary:ovpn/qlink-imt.conf


            if (path.indexOf(':') != -1 && path.lastIndexOf('/') > path.indexOf(':')) {
                String possibleDir = path.substring(path.indexOf(':') + 1, path.length());
                // Unquote chars in the  path
                try {
                    possibleDir = URLDecoder.decode(possibleDir, "UTF-8");
                } catch (UnsupportedEncodingException ignored) {
                }

                possibleDir = possibleDir.substring(0, possibleDir.lastIndexOf('/'));


                dirlist.add(new File(sdcard, possibleDir));

            }
            dirlist.add(new File(path));


        }
        dirlist.add(sdcard);
        dirlist.add(root);


        String[] fileparts = filename.split("/");
        for (File rootdir : dirlist) {
            String suffix = "";
            for (int i = fileparts.length - 1; i >= 0; i--) {
                if (i == fileparts.length - 1) {
                    suffix = fileparts[i];
                } else {
                    suffix = fileparts[i] + "/" + suffix;
                }
                File possibleFile = new File(rootdir, suffix);
                if (possibleFile.canRead()) {
                    return possibleFile;
                }

            }
        }
        return null;
    }

    private void saveProfile() {
        KLog.i("保存配置文件");
        Intent result = new Intent();
        ProfileManager vpl = ProfileManager.getInstance(AppConfig.getInstance());

        if (!TextUtils.isEmpty(mEmbeddedPwFile)) {
            ConfigParser.useEmbbedUserAuth(mResult, mEmbeddedPwFile);
        }

        vpl.addProfile(mResult);
        vpl.saveProfile(AppConfig.getInstance(), mResult);
        vpl.saveProfileList(AppConfig.getInstance());
        result.putExtra(VpnProfile.EXTRA_PROFILEUUID, mResult.getUUID().toString());
        mView.startOrStopVPN(mResult);
//        setResult(RESULT_OK, result);
//        finish();
    }

    @Override
    public void zsFreeNum(Map map) {
        httpAPIWrapper.zsFreeNum(map)
                .subscribe(new HttpObserver<FreeNum>() {
                    @Override
                    public void onNext(FreeNum baseBack) {
                        mView.onGetFreeNumBack(baseBack.getData().getFreeNum());
                        onComplete();
                    }
                });
    }
    @Override
    public void freeConnection(Map map) {
        httpAPIWrapper.freeConnection(map)
                .subscribe(new HttpObserver<FreeNum>() {
                    @Override
                    public void onNext(FreeNum freeNum) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetFreeNumBack(freeNum.getData().getFreeNum());
                        onComplete();
                    }
                });
    }

    @Override
    public void getWalletBalance(Map map) {
        httpAPIWrapper.getBalance(map)
                .subscribe(new HttpObserver<Balance>() {
                    @Override
                    public void onNext(Balance balance) {
                        //isSuccesse
                        KLog.i("onSuccesse");
                        mView.onGetBalancelSuccess(balance);
                    }
                });
    }
    @Override
    public void reportVpnInfo(Map map) {
        httpAPIWrapper.reportVpnInfo(map)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack result) throws Exception {
                        //isSuccesse
                        KLog.i("onSuccesse");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //onError
                        KLog.i("onError");
                        throwable.printStackTrace();
                        //mView.closeProgressDialog();
                        //ToastUtil.show(mFragment.getActivity(), mFragment.getString(R.string.loading_failed_1));
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
    }
}