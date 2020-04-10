package com.stratagile.qlink.qlink;

import android.os.Environment;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.TransactionRecord;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
import com.stratagile.qlink.entity.eventbus.JoinGroupChatSuccess;
import com.stratagile.qlink.entity.eventbus.MyStatus;
import com.stratagile.qlink.entity.eventbus.ServerVpnSendComplete;
import com.stratagile.qlink.entity.im.InviteToGroupChatReq;
import com.stratagile.qlink.entity.im.InviteToGroupChatRsp;
import com.stratagile.qlink.entity.qlink.DefaultRsp;
import com.stratagile.qlink.entity.qlink.GratuitySuccess;
import com.stratagile.qlink.entity.qlink.QlinkEntity1;
import com.stratagile.qlink.entity.qlink.RecordSaveReq;
import com.stratagile.qlink.entity.qlink.RecordSaveRsp;
import com.stratagile.qlink.entity.qlink.SendVpnFileList;
import com.stratagile.qlink.entity.qlink.SendVpnFileNew;
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
    }

    /**
     * 接收到了c层发送过来的消息，统一在这里先进行预处理
     *
     * @param message      发送过来的消息内容
     * @param friendNumber 好友的位置
     */
//    public void handlerFriendMessage(String message, String friendNumber) {
//        Gson gson = new Gson();
//        try {
//            QlinkEntity qlinkEntity = gson.fromJson(message, QlinkEntity.class);
//            switch (qlinkEntity.getType()) {
////                case ConstantValue.checkConnectReq:
////                    QlinkUtil.parseMap2StringAndSend(friendNumber, ConstantValue.checkConnectRsp, new HashMap());
////                    break;
//                case ConstantValue.checkConnectRsp:
//                    CheckConnectRsp checkConnectRsp = gson.fromJson(qlinkEntity.getData().toString(), CheckConnectRsp.class);
//                    EventBus.getDefault().post(checkConnectRsp);
//                    break;
//
//                case ConstantValue.sendVpnFileRsp:
//                    QlinkEntity1 qlinkEntity1 = gson.fromJson(message, QlinkEntity1.class);
//                    ServerVpnSendComplete vpnSendEnd = new ServerVpnSendComplete();
//                    KLog.i("vpnServerFileRsp0:"+qlinkEntity.getData());
//                    VpnServerFileRsp vpnServerFileRsp = gson.fromJson(qlinkEntity1.getData(), VpnServerFileRsp.class);
//                    KLog.i("vpnServerFileRsp1:"+vpnServerFileRsp);
//                    vpnSendEnd.setData(vpnServerFileRsp);
//                    EventBus.getDefault().post(vpnSendEnd);
//                    break;
//
//                case ConstantValue.vpnUserPassAndPrivateKeyReq:
//                    VpnUserPassAndPrivateKeyReq vpnUserPassAndPrivateKeyReq = gson.fromJson(qlinkEntity.getData().toString(), VpnUserPassAndPrivateKeyReq.class);
//                    handleVpnUserAndPasswordAndPrivateKeyReq(friendNumber, vpnUserPassAndPrivateKeyReq);
//                    break;
//
//                case ConstantValue.vpnUserAndPasswordReq:
//                    VpnUserAndPasswordReq vpnUserAndPasswordReq = gson.fromJson(qlinkEntity.getData().toString(), VpnUserAndPasswordReq.class);
//                    handleVpnUserAndPasswordReq(friendNumber, vpnUserAndPasswordReq);
//                    break;
//                case ConstantValue.vpnUserAndPasswordRsp:
//                    VpnUserAndPasswordRsp vpnUserAndPasswordRsp = gson.fromJson(qlinkEntity.getData().toString(), VpnUserAndPasswordRsp.class);
//                    handleVpnUserAndPasswordRsp(friendNumber, vpnUserAndPasswordRsp);
//                    break;
//
//                case ConstantValue.vpnPrivateKeyReq:
//                    VpnPrivateKeyReq vpnPrivateKeyReq = gson.fromJson(qlinkEntity.getData().toString(), VpnPrivateKeyReq.class);
//                    handleVpnPrivateKeyReq(friendNumber, vpnPrivateKeyReq);
//                    break;
//                case ConstantValue.vpnPrivateKeyRsp:
//                    VpnPrivateKeyRsp vpnPrivateKeyRsp = gson.fromJson(qlinkEntity.getData().toString(), VpnPrivateKeyRsp.class);
//                    handleVpnPrivateKeyRsp(friendNumber, vpnPrivateKeyRsp);
//                    break;
//
//                case ConstantValue.joinGroupChatReq:
//                    InviteToGroupChatReq inviteToGroupChatReq = gson.fromJson(qlinkEntity.getData().toString(), InviteToGroupChatReq.class);
//                    handleInviteToGroupChatReq(friendNumber, inviteToGroupChatReq);
//                    break;
//                case ConstantValue.joinGroupChatRsp:
//                    InviteToGroupChatRsp inviteToGroupChatRsp = gson.fromJson(qlinkEntity.getData().toString(), InviteToGroupChatRsp.class);
//                    EventBus.getDefault().post(new JoinGroupChatSuccess(inviteToGroupChatRsp.getGroupNum()));
//                    break;
//
//                case ConstantValue.recordSaveReq:
//                    RecordSaveReq recordSaveReq = gson.fromJson(qlinkEntity.getData().toString(), RecordSaveReq.class);
//                    handleRecordSaveReq(friendNumber, recordSaveReq);
//                    break;
//                case ConstantValue.recordSaveRsp:
//                    RecordSaveRsp recordSaveRsp = gson.fromJson(qlinkEntity.getData().toString(), RecordSaveRsp.class);
//                    handleRecordSaveRsp(friendNumber, recordSaveRsp);
//                    break;
//
//                case ConstantValue.sendVpnFileListRsp:
//                    handlerSendVpnFileListRsp(friendNumber, gson.fromJson(message, SendVpnFileList.class));
//                    break;
//                case ConstantValue.sendVpnFileNewRsp:
//                    handlerSendVpnFileNewRsp(friendNumber, gson.fromJson(message, SendVpnFileNew.class));
////                    handlerSendVpnFileNewRsp(friendNumber, JSONObject.parseObject(qlinkEntity.getData().toString(), SendVpnFileNew.class));
//                    break;
//
//                case ConstantValue.defaultRsp:
//                    //处理低版本没有这个type，的返回，可以在这里做默认处理。。
////                    DefaultRsp defaultRsp = gson.fromJson(qlinkEntity.getData(), DefaultRsp.class);
//                    break;
//                default:
//                    KLog.i("没有匹配上。。。");
//                    Map<String, Object> infoMap = new HashMap<>();
//                    infoMap.put("type", qlinkEntity.getType());
//                    QlinkUtil.parseMap2StringAndSend(friendNumber, ConstantValue.defaultRsp, infoMap);
//                    break;
//            }
//        } catch (Exception e) {
//            KLog.i("数据解析错误");
//            e.printStackTrace();
//        }
//
//    }

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
                transactionRecord.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, true));
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
        infoMap.put("isMainNet", SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, true)? VpnUserAndPasswordReq.mainNet : VpnUserAndPasswordReq.testNet);
        QlinkUtil.parseMap2StringAndSendOld(friendNum, ConstantValue.vpnUserAndPasswordReq, infoMap);
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
                QlinkUtil.parseMap2StringAndSendOld(friendNum, ConstantValue.vpnPrivateKeyRsp, infoMap);
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
        infoMap.put("isMainNet", SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, true)? VpnUserAndPasswordReq.mainNet : VpnUserAndPasswordReq.testNet);
        QlinkUtil.parseMap2StringAndSendOld(friendNum, ConstantValue.vpnPrivateKeyReq, infoMap);
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

    public void sendVpnFileListReq(String friendNum) {
        Map<String, Object> infoMap = new HashMap<>();
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.sendVpnFileListReq, infoMap);
    }

    public void handlerSendVpnFileListRsp(String friendNum, SendVpnFileList sendVpnFileList) {
        KLog.i(sendVpnFileList.toString());
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("msgid", sendVpnFileList.getData().getMsgid());
        infoMap.put("offset", sendVpnFileList.getData().getOffset());
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.sendVpnFileListRsp, infoMap);
        EventBus.getDefault().post(sendVpnFileList);
    }

    public void handlerSendVpnFileNewRsp(String friendNum, SendVpnFileNew sendVpnFileNew) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("msgid", sendVpnFileNew.getData().getMsgid());
        infoMap.put("offset", sendVpnFileNew.getData().getOffset());
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.sendVpnFileNewRsp, infoMap);
        EventBus.getDefault().post(sendVpnFileNew);
    }

    /**
     * 发送vpn配置文件的请求
     */
    public void sendVpnFileRequest(String friendNum, String filePath, String vpnName) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("filePath", filePath);
        infoMap.put("vpnName", vpnName);
        QlinkUtil.parseMap2StringAndSendOld(friendNum, ConstantValue.sendVpnFileRequest, infoMap);
    }

    public void sendVpnFileNewReq(String friendNum, String vpnName) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("vpnName", vpnName);
        infoMap.put("register", 0);
        QlinkUtil.parseMap2StringAndSend(friendNum, ConstantValue.sendVpnFileNewReq, infoMap);
        KLog.i("请求发送新版本的配置文件");
    }


    public void getFriendSharedVpnInfo(String friendNum, int status) {
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        boolean hasVpn = false;
        if (status > 0) {
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.getP2pId() != null && vpnEntity.getP2pId().contains(friendNum)) {
                    hasVpn = true;
//                    KLog.i("friendNum和数据库的数据匹配成功");
//                    KLog.i("friendNum=" + friendNum);
//                    KLog.i("将" + vpnEntity.getVpnName() + "的在线状态设置为在线");
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

    public static void main(String[] args) {
        Gson gson = new Gson();
        String string1 = "{\"type\":\"sendVpnFileNewRsp\",\"data\":{\"msgid\":18,\"msglen\":5761,\"md5\":\"328bfe622565d20a7dceb74257f23302\",\"more\":0,\"offset\":0,\"msg\":\"\\nclient\\nnobind\\ndev tun\\nremote-cert-tls server\\n\\nremote 121.60.36.234 18888 udp\\n\\n<key>\\n-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDIXQ8oYrtB2l4n\\ndHeWmpqPZAGhSyIaqieqFOL3D5ol17bKV5gCZ2Qgbe3ly8NMy8Lkbs0vVX94ftcf\\nHFhzLH3iLpM6X0nQKfG/TYtPLkrhTjJpq+wuERrDKB/Jn6wgwyFIU9Lcpyztg3YB\\n/RmfMylYhpuYJESW+IjUBb1R2yZxjvx4qLGImb2nUSzYJ/IhDrxIxsHHFmuxzzZz\\nSjXoztxQpQgsRFwdqq3QtYrhOIzjp1rninerAykMbdMWVSJ7d6NckdvhSqETm5Bv\\nez5rynA1e1CkJ1KFXfDZsE8XiG02WTjBKTOSZubdyZu527XO6BZ563saR7b4PhSN\\nsLUdZ39nAgMBAAECggEBAIal0yvmvcTRhPiq0jsJhtjZ8iZ8oVyeAK3R/3zcpVDN\\nGG/+UY88ABOzDG6jconHXR+6PnWS1WkahGLJ3772pVo8xoAxzR7xMR7Ic9gwWe3z\\naPmOqdeDcyK3cjVC8p/JwjIi8s+KIS00bTeE6ZUNAroVK7cgmF+Egh9KBCJgTgN9\\nTIkeQ6M3pgmLTGxODuk/2z5GWUNYyNK70sJOyaby0J3/648hVUDYcQ+7iAve9311\\nKxmkShsPkhqcZ2L78C3M8rBpVgSGYjXAtVIKfR5B++K02XO3LGy1JcanH5boWZs5\\nq49er/UdgRaDz2t8sVCengGSO2njXJWPHv8dsXh/SMECgYEA82AvR6mhIMbbHrtx\\nMpHsGmob/4l4vAGmOaffaM7Nfc6wvoeXOJRz8xaOtOTiY14kXBWB4PoCg+8PT8mz\\nriUZI1gssVs8l2AKaqcQ8rKkHACNc8GsVoXrScQVhC\"}}";
        String string2 = "{\"type\":\"sendVpnFileNewRsp\",\"data\":\"{\"msgid\":18,\"msglen\":5761,\"md5\":\"328bfe622565d20a7dceb74257f23302\",\"more\":0,\"offset\":0,\"msg\":\"\\nclient\\nnobind\\ndev tun\\nremote-cert-tls server\\n\\nremote 121.60.36.234 18888 udp\\n\\n<key>\\n-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDIXQ8oYrtB2l4n\\ndHeWmpqPZAGhSyIaqieqFOL3D5ol17bKV5gCZ2Qgbe3ly8NMy8Lkbs0vVX94ftcf\\nHFhzLH3iLpM6X0nQKfG/TYtPLkrhTjJpq+wuERrDKB/Jn6wgwyFIU9Lcpyztg3YB\\n/RmfMylYhpuYJESW+IjUBb1R2yZxjvx4qLGImb2nUSzYJ/IhDrxIxsHHFmuxzzZz\\nSjXoztxQpQgsRFwdqq3QtYrhOIzjp1rninerAykMbdMWVSJ7d6NckdvhSqETm5Bv\\nez5rynA1e1CkJ1KFXfDZsE8XiG02WTjBKTOSZubdyZu527XO6BZ563saR7b4PhSN\\nsLUdZ39nAgMBAAECggEBAIal0yvmvcTRhPiq0jsJhtjZ8iZ8oVyeAK3R/3zcpVDN\\nGG/+UY88ABOzDG6jconHXR+6PnWS1WkahGLJ3772pVo8xoAxzR7xMR7Ic9gwWe3z\\naPmOqdeDcyK3cjVC8p/JwjIi8s+KIS00bTeE6ZUNAroVK7cgmF+Egh9KBCJgTgN9\\nTIkeQ6M3pgmLTGxODuk/2z5GWUNYyNK70sJOyaby0J3/648hVUDYcQ+7iAve9311\\nKxmkShsPkhqcZ2L78C3M8rBpVgSGYjXAtVIKfR5B++K02XO3LGy1JcanH5boWZs5\\nq49er/UdgRaDz2t8sVCengGSO2njXJWPHv8dsXh/SMECgYEA82AvR6mhIMbbHrtx\\nMpHsGmob/4l4vAGmOaffaM7Nfc6wvoeXOJRz8xaOtOTiY14kXBWB4PoCg+8PT8mz\\nriUZI1gssVs8l2AKaqcQ8rKkHACNc8GsVoXrScQVhC\"}\"}";
        QlinkEntity qlinkEntity = gson.fromJson(string1, QlinkEntity.class);
//        QlinkEntity1 qlinkEntity1 = gson.fromJson(string2, QlinkEntity1.class);

//        SendVpnFileNew sendVpnFileNew = gson.fromJson(qlinkEntity1.getData(), SendVpnFileNew.class);
//        System.out.println(sendVpnFileNew.toString());

        SendVpnFileNew sendVpnFileNew1 = gson.fromJson(qlinkEntity.getData().toString(), SendVpnFileNew.class);
        System.out.println(sendVpnFileNew1.toString());

    }
}
