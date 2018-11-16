package com.stratagile.qlink.qlink;

import android.os.Environment;

import com.google.gson.Gson;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
import com.stratagile.qlink.entity.eventbus.MyStatus;
import com.stratagile.qlink.entity.eventbus.ServerVpnSendComplete;
import com.stratagile.qlink.entity.im.InviteToGroupChatReq;
import com.stratagile.qlink.entity.qlink.DefaultRsp;
import com.stratagile.qlink.entity.qlink.GratuitySuccess;
import com.stratagile.qlink.entity.qlink.RecordSaveReq;
import com.stratagile.qlink.entity.qlink.RecordSaveRsp;
import com.stratagile.qlink.entity.qlink.SendVpnFileRequest;
import com.stratagile.qlink.entity.vpn.VpnBasicInfoReq;
import com.stratagile.qlink.entity.vpn.VpnBasicInfoRsp;
import com.stratagile.qlink.entity.vpn.VpnPrivateKeyReq;
import com.stratagile.qlink.entity.vpn.VpnPrivateKeyRsp;
import com.stratagile.qlink.entity.vpn.VpnServerFileRsp;
import com.stratagile.qlink.entity.vpn.VpnUserAndPasswordReq;
import com.stratagile.qlink.entity.vpn.VpnUserAndPasswordRsp;
import com.stratagile.qlink.entity.vpn.VpnUserPassAndPrivateKeyReq;
import com.stratagile.qlink.ui.activity.wallet.TransactionRecordActivity;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.qlink.ServiceConnectedEvent;
import com.stratagile.qlink.entity.qlink.QlinkEntity;
import com.stratagile.qlink.entity.wifi.WifiBasicinfoReq;
import com.stratagile.qlink.entity.wifi.WifiConnectSuccess;
import com.stratagile.qlink.entity.wifi.WifibasicinfoRsp;
import com.stratagile.qlink.entity.wifi.WifipasswordReq;
import com.stratagile.qlink.entity.wifi.WifipasswordRsp;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.VpnUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

/**
 * Created by huzhipeng on 2018/1/19.
 */

public class Qsdk {

    private static Qsdk instance = null;

    private Qsdk() {
    }

    public static Qsdk getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Qsdk();
                }
            }
        }
        return instance;
    }

    public static void init() {
        getInstance();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //将所有的好友的状态置为未在线
                List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
                List<VpnEntity> vpnEntityList1 = new ArrayList<>();
                for (VpnEntity vpnEntity : vpnEntityList) {
                    vpnEntity.setOnline(false);
                    vpnEntity.setGroupNum(-1);
                    vpnEntity.setFriendNum("");
                    vpnEntity.setUnReadMessageCount(0);
                    vpnEntity.setIsConnected(false);
                    vpnEntity.setIsLoadingAvater(false);
                    vpnEntity.setCurrentConnect(0);
                    if (vpnEntity.getP2pId() == null) {
                        continue;
                    }
                    if (vpnEntity.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                        vpnEntity.setAvaterUpdateTime(Long.parseLong(SpUtil.getString(AppConfig.getInstance(), ConstantValue.myAvaterUpdateTime, "0")));
                    }
                    vpnEntityList1.add(vpnEntity);
                }
                try {
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().updateInTx(vpnEntityList1);
                }catch (Exception e)
                {

                }
                List<TransactionRecord> transactionRecordList = AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll();
                for (TransactionRecord transactionRecord : transactionRecordList) {
                    //3为vpn
                    if (transactionRecord.getTransactiomType() == 3) {
                        if (transactionRecord.getConnectType() == 0 && !transactionRecord.getIsReported()) {
                            ConstantValue.unReportRecord.add(transactionRecord);
                        }
                    }
                    //0为wifi
                    if (transactionRecord.getTransactiomType() == 0) {
                        if (transactionRecord.getConnectType() == 0 && !transactionRecord.getIsReported()) {
                            ConstantValue.unReportRecord.add(transactionRecord);
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * 接收到了c层发送过来的消息，统一在这里先进行预处理
     *
     * @param message      发送过来的消息内容
     * @param friendNumber 好友的位置
     */
    public void handlerFriendMessage(String message, String friendNumber) {
        Gson gson = new Gson();
        try {
            QlinkEntity qlinkEntity = gson.fromJson(message, QlinkEntity.class);
            switch (qlinkEntity.getType()) {
                case ConstantValue.sendFileRequest:
                    KLog.i("收到发送自己头像的请求，将我自己的头像文件发送出去");
                    qlinkcom.Addfilesender(friendNumber, Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(AppConfig.getInstance(), ConstantValue.myAvaterUpdateTime, "") + ".jpg");
                    break;
                case ConstantValue.sendVpnFileRequest:
                    KLog.i("收到发送自己注册的vpn的配置文件的请求，将自己注册的vpn的配置文件发送出去");
                    SendVpnFileRequest sendVpnFileRequest = gson.fromJson(qlinkEntity.getData(), SendVpnFileRequest.class);
                    qlinkcom.Addfilesender(friendNumber, sendVpnFileRequest.getFilePath());
                    break;
                case ConstantValue.heartBetSend:
                    ServiceConnectedEvent serviceConnectedEvent = gson.fromJson(qlinkEntity.getData(), ServiceConnectedEvent.class);
                    serviceConnectedEvent.setFriendNum(friendNumber);
                    handlerHeartBet(serviceConnectedEvent);
                    break;
                case ConstantValue.vpnBasicInfoReq:
                    VpnBasicInfoReq vpnBasicInfoReq = gson.fromJson(qlinkEntity.getData(), VpnBasicInfoReq.class);
                    handleVpnBasicInfoReq(friendNumber, vpnBasicInfoReq);
                    break;
                case ConstantValue.vpnBasicInfoRsp:
                    VpnBasicInfoRsp vpnBasicInfoRsp = gson.fromJson(qlinkEntity.getData(), VpnBasicInfoRsp.class);
                    handleVpnBasicInfoRsp(vpnBasicInfoRsp);
                    break;
                case ConstantValue.vpnPrivateKeyReq:
                    VpnPrivateKeyReq vpnPrivateKeyReq = gson.fromJson(qlinkEntity.getData(), VpnPrivateKeyReq.class);
                    handleVpnPrivateKeyReq(friendNumber, vpnPrivateKeyReq);
                    break;
                case ConstantValue.vpnPrivateKeyRsp:
                    VpnPrivateKeyRsp vpnPrivateKeyRsp = gson.fromJson(qlinkEntity.getData(), VpnPrivateKeyRsp.class);
                    handleVpnPrivateKeyRsp(friendNumber, vpnPrivateKeyRsp);
                    break;
                case ConstantValue.vpnUserAndPasswordReq:
                    VpnUserAndPasswordReq vpnUserAndPasswordReq = gson.fromJson(qlinkEntity.getData(), VpnUserAndPasswordReq.class);
                    handleVpnUserAndPasswordReq(friendNumber, vpnUserAndPasswordReq);
                    break;
                case ConstantValue.vpnUserAndPasswordRsp:
                    VpnUserAndPasswordRsp vpnUserAndPasswordRsp = gson.fromJson(qlinkEntity.getData(), VpnUserAndPasswordRsp.class);
                    handleVpnUserAndPasswordRsp(friendNumber, vpnUserAndPasswordRsp);
                    break;
                case ConstantValue.vpnUserPassAndPrivateKeyReq:
                    VpnUserPassAndPrivateKeyReq vpnUserPassAndPrivateKeyReq = gson.fromJson(qlinkEntity.getData(), VpnUserPassAndPrivateKeyReq.class);
                    handleVpnUserAndPasswordAndPrivateKeyReq(friendNumber, vpnUserPassAndPrivateKeyReq);
                    break;
                case ConstantValue.recordSaveReq:
                    RecordSaveReq recordSaveReq = gson.fromJson(qlinkEntity.getData(), RecordSaveReq.class);
                    handleRecordSaveReq(friendNumber, recordSaveReq);
                    break;
                case ConstantValue.recordSaveRsp:
                    RecordSaveRsp recordSaveRsp = gson.fromJson(qlinkEntity.getData(), RecordSaveRsp.class);
                    handleRecordSaveRsp(friendNumber, recordSaveRsp);
                    break;
                case ConstantValue.gratuitySuccess://
                    GratuitySuccess gratuitySuccess = gson.fromJson(qlinkEntity.getData(), GratuitySuccess.class);
                    double qlc = gratuitySuccess.getQlc();
                    AppConfig.getInstance().showNotificationChannels(qlc, TransactionRecordActivity.class);
                    break;
                case ConstantValue.joinGroupChatReq:
                    InviteToGroupChatReq inviteToGroupChatReq = gson.fromJson(qlinkEntity.getData(), InviteToGroupChatReq.class);
                    handleInviteToGroupChatReq(friendNumber, inviteToGroupChatReq);
                    break;
                case ConstantValue.joinGroupChatRsp:
//                    InviteToGroupChatRsp inviteToGroupChatRsp = gson.fromJson(qlinkEntity.getData(), InviteToGroupChatRsp.class);
//                    EventBus.getDefault().post(new JoinGroupChatSuccess(inviteToGroupChatRsp.getGroupNum()));
                    break;
                case ConstantValue.checkConnectReq:
                    QlinkUtil.parseMap2StringAndSend(friendNumber, ConstantValue.checkConnectRsp, new HashMap());
                    break;
                case ConstantValue.checkConnectRsp:
                    EventBus.getDefault().post(new CheckConnectRsp());
                    break;
                case ConstantValue.allVpnBasicInfoReq:
                    handleAllVpnBasicInfoReq(friendNumber);
                    break;
                case ConstantValue.allVpnBasicInfoRsp:

                    break;
                case ConstantValue.sendVpnFileRsp:
                    ServerVpnSendComplete vpnSendEnd = new ServerVpnSendComplete();
                    KLog.i("vpnServerFileRsp0:"+qlinkEntity.getData());
                    VpnServerFileRsp vpnServerFileRsp = gson.fromJson(qlinkEntity.getData(), VpnServerFileRsp.class);
                    KLog.i("vpnServerFileRsp1:"+vpnServerFileRsp);
                    vpnSendEnd.setData(vpnServerFileRsp);
                    EventBus.getDefault().post(vpnSendEnd);
                    break;
                case ConstantValue.defaultRsp:
                    //处理低版本没有这个type，的返回，可以在这里做默认处理。。
                    DefaultRsp defaultRsp = gson.fromJson(qlinkEntity.getData(), DefaultRsp.class);
                    switch (defaultRsp.getType()) {
                        //这个case在1.1.6版本加上的，可以不用加。
                        case ConstantValue.checkConnectReq:
                            EventBus.getDefault().post(new CheckConnectRsp());
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    KLog.i("没有匹配上。。。");
                    Map<String, Object> infoMap = new HashMap<>();
                    infoMap.put("type", qlinkEntity.getType());
                    QlinkUtil.parseMap2StringAndSend(friendNumber, ConstantValue.defaultRsp, infoMap);
                    break;
            }
        } catch (Exception e) {
            KLog.i("数据解析错误");
            e.printStackTrace();
        }

    }

    /**
     * 将该好友添加进对应的资产的群组中
     *
     * @param friendNum
     * @param inviteToGroupChatReq
     */
    private void handleInviteToGroupChatReq(String friendNum, InviteToGroupChatReq inviteToGroupChatReq) {
        if (inviteToGroupChatReq.getAssetType() == MyAsset.VPN_ASSET_1) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (VpnUtil.isInSameNet(vpnEntity )&& vpnEntity.getVpnName().equals(inviteToGroupChatReq.getAssetName()) && vpnEntity.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
                    int result = qlinkcom.InviteFriendToGroupChat(friendNum, vpnEntity.getGroupNum());
                    if (result == 0) {
                    }
                    return;
                }
            }
        } else if (inviteToGroupChatReq.getAssetType() == MyAsset.WIFI_ASSET) {
        }
    }

    /**
     * 处理没有完成上报的记录
     */
    public void handleUnReportRecord(String friendNum) {
        if (ConstantValue.unReportRecord == null || ConstantValue.unReportRecord.size() == 0) {
            KLog.i("没有上报的记录量为0");
            return;
        }
        byte[] p2pId = new byte[100];
        qlinkcom.GetFriendP2PPublicKey(friendNum, p2pId);
        KLog.i("好友上线了，friendNum= " + friendNum + " 他的p2pId是：");
        KLog.i(new String(p2pId).trim());
        for (TransactionRecord transactionRecord : ConstantValue.unReportRecord) {
            if (transactionRecord.getToP2pId() != null && transactionRecord.getToP2pId().equals(p2pId)) {
                sendRecordSaveReq(friendNum, transactionRecord);
            }
        }
    }

    /**
     * 处理保存上报记录给提供者的返回
     *
     * @param friendNum
     * @param recordSaveRsp
     */
    public void handleRecordSaveRsp(String friendNum, RecordSaveRsp recordSaveRsp) {
        List<TransactionRecord> transactionRecordList = AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll();
        for (TransactionRecord transactionRecord : transactionRecordList) {
            if (transactionRecord.getTxid().equals(recordSaveRsp.getTxid()) && transactionRecord.getConnectType() == 0) {
                transactionRecord.setIsReported(true);
                transactionRecord.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
                AppConfig.getInstance().getDaoSession().getTransactionRecordDao().update(transactionRecord);
                break;
            }
        }
        for (TransactionRecord transactionRecord : ConstantValue.unReportRecord) {
            if (transactionRecord.getTxid().equals(recordSaveRsp.getTxid())) {
                ConstantValue.unReportRecord.remove(transactionRecord);
                break;
            }
        }
    }

    public void handleRecordSaveReq(String friendNum, RecordSaveReq recordSaveReq) {
        List<TransactionRecord> transactionRecordList = AppConfig.getInstance().getDaoSession().getTransactionRecordDao().loadAll();
        for (TransactionRecord transactionRecord : transactionRecordList) {
            if (transactionRecord.getTxid().equals(recordSaveReq.getTxid())) {
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("txid", transactionRecord.getTxid());
                infoMap.put("success", "1");
                QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.recordSaveRsp, infoMap);
                return;
            }
        }
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setTxid(recordSaveReq.getTxid());
        transactionRecord.setExChangeId(recordSaveReq.getExChangeId());
        transactionRecord.setAssetName(recordSaveReq.getAssetName());
        transactionRecord.setTransactiomType(recordSaveReq.getTransactiomType());
        transactionRecord.setConnectType(1);
        transactionRecord.setIsReported(true);
        transactionRecord.setTimestamp(recordSaveReq.getTimestamp());
        transactionRecord.setIsMainNet(recordSaveReq.getIsMainNet().equals(VpnUserAndPasswordReq.mainNet));
        AppConfig.getInstance().getDaoSession().getTransactionRecordDao().insert(transactionRecord);
        //告诉使用端，我已经记录成功，
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("txid", transactionRecord.getTxid());
        infoMap.put("success", "1");
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.recordSaveRsp, infoMap);
        if (recordSaveReq.getTransactiomType() == 3)//如果是vpn连接，说明有qlc进账
        {
            AppConfig.getInstance().showNotificationChannels(recordSaveReq.getQlcCount(), TransactionRecordActivity.class);//vpn收益通知
        }
    }

    /**
     * 连接资产之后做记录的请求
     *
     * @param transactionRecord
     */
    public void sendRecordSaveReq(String friendNum, TransactionRecord transactionRecord) {
        KLog.i("给" + friendNum + "添加记录");
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("transactiomType", transactionRecord.getTransactiomType());
        infoMap.put("txid", transactionRecord.getTxid());
        infoMap.put("exChangeId", transactionRecord.getExChangeId());
        infoMap.put("timestamp", transactionRecord.getTimestamp());
        infoMap.put("assetName", transactionRecord.getAssetName());
        infoMap.put("qlcCount", transactionRecord.getQlcCount());
        infoMap.put("isMainNet", transactionRecord.getIsMainNet()? VpnUserAndPasswordReq.mainNet : VpnUserAndPasswordReq.testNet);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.recordSaveReq, infoMap);
    }

    /**
     * 处理vpn账号和密码的返回
     *
     * @see com.stratagile.qlink.ui.activity.vpn.ConnectVpnActivity#userNameAndPasswordBack(VpnUserAndPasswordRsp)
     */
    public void handleVpnUserAndPasswordRsp(String friendNum, VpnUserAndPasswordRsp vpnUserAndPasswordRsp) {
        EventBus.getDefault().post(vpnUserAndPasswordRsp);
    }

    /**
     * 处理vpn账号和密码的请求
     */
    public void handleVpnUserAndPasswordReq(String friendNum, VpnUserAndPasswordReq vpnUserAndPasswordReq) {
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (vpnEntity.getVpnName().equals(vpnUserAndPasswordReq.getVpnName())) {
                if (!VpnUtil.isInSameNet(vpnUserAndPasswordReq.getIsMainNet(), vpnEntity)) {
                    continue;
                }
                LogUtil.addLog("发送我自己的vpn的用户名和密码  vpnNmae=" + vpnEntity.getVpnName() + "  userName=" + vpnEntity.getUsername() + "  password=" + vpnEntity.getPassword(), getClass().getSimpleName());
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("vpnName", vpnEntity.getVpnName());
                infoMap.put("userName", vpnEntity.getUsername());
                infoMap.put("password", vpnEntity.getPassword());
                QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnUserAndPasswordRsp, infoMap);
                return;
            }
        }
    }
    /**
     * 处理vpn账号和密码和私钥的请求
     */
    public void handleVpnUserAndPasswordAndPrivateKeyReq(String friendNum, VpnUserPassAndPrivateKeyReq vpnUserPassAndPrivateKeyReq) {
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (vpnEntity.getVpnName().equals(vpnUserPassAndPrivateKeyReq.getVpnName())) {
                if (!VpnUtil.isInSameNet(vpnUserPassAndPrivateKeyReq.getIsMainNet(), vpnEntity)) {
                    continue;
                }
                LogUtil.addLog("发送我自己的vpn的用户名和密码  vpnNmae=" + vpnEntity.getVpnName() + "  userName=" + vpnEntity.getUsername() + "  password=" + vpnEntity.getPassword(), getClass().getSimpleName());
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("vpnName", vpnEntity.getVpnName());
                infoMap.put("userName", vpnEntity.getUsername());
                infoMap.put("password", vpnEntity.getPassword());
                infoMap.put("privateKey", vpnEntity.getPrivateKeyPassword());
                QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnUserPassAndPrivateKeyRsp, infoMap);
                return;
            }
        }
    }

    /**
     * 发送vpn账号和密码的请求
     */
    public void sendVpnUserAndPasswordReq(String friendNum, String vpnName) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("vpnName", vpnName);
        infoMap.put("isMainNet", SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)? VpnUserAndPasswordReq.mainNet : VpnUserAndPasswordReq.testNet);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnUserAndPasswordReq, infoMap);
    }

    /**
     * 处理vpn私钥的请求
     */
    public void handleVpnPrivateKeyReq(String friendNum, VpnPrivateKeyReq vpnPrivateKeyReq) {
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (vpnEntity.getVpnName().equals(vpnPrivateKeyReq.getVpnName())) {
                if (!VpnUtil.isInSameNet(vpnPrivateKeyReq.getIsMainNet(), vpnEntity)) {
                    continue;
                }
                LogUtil.addLog("发送我自己的vpn的私钥  vpnNmae=" + vpnEntity.getVpnName() + "  privatekey=" + vpnEntity.getPrivateKeyPassword(), getClass().getSimpleName());
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("vpnName", vpnEntity.getVpnName());
                infoMap.put("privateKey", vpnEntity.getPrivateKeyPassword());
                QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnPrivateKeyRsp, infoMap);
                return;
            }
        }
    }

    /**
     * 发送vpn私钥的请求
     */
    public void sendVpnPrivateKeyReq(String friendNum, String vpnName) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("vpnName", vpnName);
        infoMap.put("isMainNet", SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)? VpnUserAndPasswordReq.mainNet : VpnUserAndPasswordReq.testNet);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnPrivateKeyReq, infoMap);
    }

    /**
     * 处理vpn私钥的返回
     *
     * @see com.stratagile.qlink.ui.activity.vpn.ConnectVpnActivity#privateKeyBack(VpnPrivateKeyRsp)
     */
    public void handleVpnPrivateKeyRsp(String friendNum, VpnPrivateKeyRsp vpnPrivateKeyRsp) {
        KLog.i("收到了私钥的返回，发送evnentbus消息给使用方");
        EventBus.getDefault().post(vpnPrivateKeyRsp);
    }


    /**
     * 处理vpn基础信息的请求
     *
     * @param vpnBasicInfoReq
     */
    private void handleVpnBasicInfoReq(String friendNum, VpnBasicInfoReq vpnBasicInfoReq) {
        if (vpnBasicInfoReq.getP2pId() != null && vpnBasicInfoReq.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (VpnUtil.isInSameNet(vpnEntity ) && vpnEntity.getVpnName().equals(vpnBasicInfoReq.getVpnName())) {
                    sendVpnBasicInfoRsp(friendNum, vpnEntity);
                    return;
                }
            }
//            Map<String, Object> infoMap = new HashMap<>();
//            infoMap.put("vpnName", vpnBasicInfoReq.getVpnName());
//            infoMap.put("exist", false);
//            QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnBasicInfoRsp, infoMap);
        }
        if (vpnBasicInfoReq.getP2pId() == null) {
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (VpnUtil.isInSameNet(vpnEntity ) && vpnEntity.getVpnName().equals(vpnBasicInfoReq.getVpnName())) {
                    sendVpnBasicInfoRsp(friendNum, vpnEntity);
                    return;
                }
            }
//            Map<String, Object> infoMap = new HashMap<>();
//            infoMap.put("vpnName", vpnBasicInfoReq.getVpnName());
//            infoMap.put("exist", false);
//            QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnBasicInfoRsp, infoMap);
        }
    }

    /**
     * 将自己的所有vpn资产的基础信息发送出去
     */
    private void handleAllVpnBasicInfoReq(String friendNum) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("vpn", LocalAssetsUtils.getVpnList());
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.allVpnBasicInfoRsp, infoMap);
    }

    /**
     * 发送vpn基础信息的返回
     *
     * @param friendNum
     * @param vpnEntity
     */
    private void sendVpnBasicInfoRsp(String friendNum, VpnEntity vpnEntity) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("vpnName", vpnEntity.getVpnName());
        infoMap.put("p2pId", vpnEntity.getP2pId());
        infoMap.put("country", vpnEntity.getCountry());
        infoMap.put("continent", vpnEntity.getContinent());
        infoMap.put("connectMaxnumber", vpnEntity.getConnectMaxnumber());
        infoMap.put("profileLocalPath", vpnEntity.getProfileLocalPath());
        infoMap.put("bandwidth", vpnEntity.getBandwidth());
        infoMap.put("currentConnect", vpnEntity.getCurrentConnect());
        infoMap.put("avaterUpdateTime", vpnEntity.getAvaterUpdateTime());
        infoMap.put("qlc", vpnEntity.getQlc());
        infoMap.put("exist", true);
        infoMap.put("ipV4Address", vpnEntity.getIpV4Address());
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.vpnBasicInfoRsp, infoMap);
    }

    /**
     * 处理vpn基础信息的返回
     *
     * @param vpnBasicInfoRsp
     */
    public void handleVpnBasicInfoRsp(VpnBasicInfoRsp vpnBasicInfoRsp) {
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (VpnUtil.isInSameNet(vpnEntity )&&vpnEntity.getVpnName().equals(vpnBasicInfoRsp.getVpnName())) {
                if (vpnBasicInfoRsp.isExist()) {
                    vpnEntity.setConnectMaxnumber(vpnBasicInfoRsp.getConnectMaxnumber());
                    vpnEntity.setProfileLocalPath(vpnBasicInfoRsp.getProfileLocalPath());
                    vpnEntity.setBandwidth(vpnBasicInfoRsp.getBandwidth());
                    vpnEntity.setAvaterUpdateTime(vpnBasicInfoRsp.getAvaterUpdateTime());
                    vpnEntity.setCurrentConnect(vpnBasicInfoRsp.getCurrentConnect());
                    vpnEntity.setIpV4Address(vpnBasicInfoRsp.getIpV4Address());
                    vpnEntity.setQlc(vpnBasicInfoRsp.getQlc());
                    if (vpnBasicInfoRsp.getContinent() != null) {
                        vpnEntity.setContinent(vpnBasicInfoRsp.getContinent());
                    }
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    return;
                } else if (VpnUtil.isInSameNet(vpnEntity )&& vpnEntity.getVpnName().equals(vpnBasicInfoRsp.getVpnName()) && !vpnBasicInfoRsp.isExist()) {
//                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().delete(vpnEntity);
                    return;
                }
            }
        }
    }

    /**
     * 发送vpn配置文件的请求
     */
    public void sendVpnFileRequest(String friendNum, String filePath, String vpnName) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("filePath", filePath);
        infoMap.put("vpnName", vpnName);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.sendVpnFileRequest, infoMap);
    }

    /**
     * 心跳包发送
     *
     * @see com.stratagile.qlink.service.ClientConnectedWifiRecordService
     */
    public void heartBetSend(int eventType, int useType, String ssid, String friendNum) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("ssid", ssid);
        infoMap.put("friendNum", friendNum);
        infoMap.put("eventType", eventType);
        infoMap.put("useType", useType);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.heartBetSend, infoMap);
    }

    /**
     * 处理心跳包
     *
     * @see com.stratagile.qlink.service.ServiceConnectedWIfiRecordSevice#handlerEvent(ServiceConnectedEvent)
     */
    private void handlerHeartBet(ServiceConnectedEvent serviceConnectedEvent) {
        EventBus.getDefault().post(serviceConnectedEvent);
    }





    public void getFriendSharedVpnInfo(String friendNum, int status) {
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        boolean hasVpn = false;
        if (status > 0) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.getP2pId() != null && vpnEntity.getP2pId().contains(friendNum)) {
                    hasVpn = true;
                    KLog.i("friendNum和数据库的数据匹配成功");
                    KLog.i("friendNum=" + friendNum);
                    KLog.i("将" + vpnEntity.getVpnName() + "的在线状态设置为在线");
                    vpnEntity.setFriendNum(friendNum);
                    vpnEntity.setOnline(true);
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    //好友状态改变，更新ui
                    //这里不能break，因为一个好友可能注册多个WiFi资产，
                    //break;
                }
            }
            if (hasVpn) {
                EventBus.getDefault().post(new ArrayList<VpnEntity>());
            }
        } else {
            for (VpnEntity vpnEntity : vpnEntityList) {
                hasVpn = true;
                if (vpnEntity.getP2pId().contains(friendNum)) {
                    vpnEntity.setOnline(false);
                    KLog.i("将" + vpnEntity.getVpnName() + "的在线状态设置为不在线");
                    LogUtil.addLog("将" + vpnEntity.getVpnName() + "的在线状态设置为不在线", getClass().getSimpleName());
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                }
            }
            if (hasVpn) {
                EventBus.getDefault().post(new ArrayList<VpnEntity>());
            }
        }
    }

    /**
     * 由外部调用
     * 获取好友当前连接的WiFi的信息
     * 当好友上线的时候调用，或者心跳的时候调用
     * 如果他连接的WiFi是我分享的WiFi，那么我分享的WiFi的使用者就要加1
     */
    public void getFriendCurrentConnectWifiInfo(String friendNum, int status) {
        if (status > 0) {
            Map<String, Object> infoMap = new HashMap<>();
            infoMap.put("ssid", "");
            infoMap.put("mac", "");
            QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.wifiCurrentWifiInfoReq, infoMap);
        }
    }

    /**
     * 处理获取当前连接的WiFi的信息的请求
     *
     * @see#getFriendCurrentConnectWifiInfo
     */
    private void handlerGetCurrentWifiInfoRequest(String friendNum) {
        Map<String, Object> infoMap = new HashMap<>();
        String ssid = ConstantValue.connectedWifiInfo.getSSID().replace("\"", "");
        if (ssid.contains("0x")) {
            ssid = "";
        }
        KLog.i(ssid);
        infoMap.put("ssid", ssid);
        infoMap.put("mac", ConstantValue.connectedWifiInfo.getBSSID());
        KLog.i("当前wifi返回的数据为:" + infoMap);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.wifiCurrentWifiInfoRsp, infoMap);
    }

    /**
     * 获取该好友的当前连接的WiFi的返回
     *
     * @param friendNum
     */
    private void handlerGetCurrentWifiInfoResponse(String friendNum, WifibasicinfoRsp wifibasicinfoRsp) {
        EventBus.getDefault().post(new ServiceConnectedEvent(0, 0, wifibasicinfoRsp.getSsid(), friendNum));
//        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
//        for (WifiEntity wifiEntity : wifiEntityList) {
//                if (wifiEntity.getSsid().equals(wifibasicinfoRsp.getSsid())) {
//                    if (wifiEntity.getOwnerP2PId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
//                        wifiEntity.setConnectCount(wifiEntity.getConnectCount() + 1);
//                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
//                        EventBus.getDefault().post(new ArrayList<WifiEntity>());
//                        //告诉该好友，自己的WiFi的连接数量
//                        sendWifiBaseInfoRsp(wifiEntity, friendNum);
//                        return;
//                    }
//                }
//        }
    }


    /**
     * 添加传送文件的请求，
     */
    public int addFileSendRequest(String freindNumber) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("friendNumber", freindNumber);
        int sendResult = QlinkUtil.parseMap2StringAndSend(freindNumber, ConstantValue.sendFileRequest, infoMap);
        if (sendResult == 0) {
            ConstantValue.isLoadingImg = true;
        }
        return sendResult;
    }


    /**
     * 更新自己注册的WiFi的信息
     *
     * @param status
     * @see qlinkcom#CallSelfChange(int)
     * @see com.stratagile.qlink.ui.activity.main.MainActivity#setMyStatus(MyStatus)
     */
    public void handlerSelfStatusChange(int status) {
        EventBus.getDefault().post(new MyStatus(status));
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "").equals(vpnEntity.getP2pId())) {
                if (status > 0) {
                    vpnEntity.setOnline(true);
                    if (!ConstantValue.isLogin) {
                        KLog.i("为" + vpnEntity.getVpnName() + "  创建群组。。。");
                        int groupNum = qlinkcom.CreatedNewGroupChat();
                        if (groupNum >= 0) {
                            vpnEntity.setGroupNum(groupNum);
                        } else {
                            groupNum = qlinkcom.CreatedNewGroupChat();
                            vpnEntity.setGroupNum(groupNum);
                        }
                    }
                } else {
                    vpnEntity.setOnline(false);
                }
                AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
            }
        }
        ConstantValue.isLogin = true;
        EventBus.getDefault().post(new ArrayList<VpnEntity>());
    }

    /**
     * wifi使用者给提供者打赏成功的通知
     *
     * @param friendNum 好友id
     * @param qlc       打赏金额
     */
    public void gratuitySuccess(String friendNum, double qlc) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("qlc", qlc);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.gratuitySuccess, infoMap);
    }
}
