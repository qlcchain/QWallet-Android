package com.stratagile.qlink;

import android.os.Environment;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.eventbus.JoinNewGroup;
import com.stratagile.qlink.entity.eventbus.MyStatus;
import com.stratagile.qlink.entity.eventbus.VpnSendEnd;
import com.stratagile.qlink.entity.im.Message;
import com.stratagile.qlink.qlink.P2PCallBack;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhipeng on 2018/1/8.
 */

public class qlinkcom {

    /**
     * 初始化的结果
     */

    private static int initResult = -1;

    static {
        try {
            System.loadLibrary("sodium");
            System.loadLibrary("qlinkp2p");

        } catch (Throwable  e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化sdk
     */
    public static void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink", "");
                if (!dataFile.exists()) {
                    dataFile.mkdir();
                }
                File cacheVpnFile = new File(AppConfig.getInstance().getFilesDir() + "Qlink/vpn", "");
                if (!cacheVpnFile.exists()) {
                    cacheVpnFile.mkdir();
                }
                File vpnFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/vpn", "");
                if (!vpnFile.exists()) {
                    vpnFile.mkdir();
                }
                File imageFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image", "");
                if (!imageFile.exists()) {
                    imageFile.mkdir();
                }
                File addressFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/Address", "");
                if (!addressFile.exists()) {
                    addressFile.mkdir();
                }
                File ethKeystore = new File(Environment.getExternalStorageDirectory() + "/Qlink/KeyStore", "");
                if (!ethKeystore.exists()) {
                    ethKeystore.mkdir();
                }
                File profileFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/Profile", "");
                if (!profileFile.exists()) {
                    profileFile.mkdir();
                }
                //用于资产永久化存储
                File assetsFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/Assets", "");
                if (!assetsFile.exists()) {
                    assetsFile.mkdir();
                }
                File backupFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/backup", "");
                if (!backupFile.exists()) {
                    backupFile.mkdir();
                }
                String path = dataFile.getPath() + "/";
                try {
                    if (qlinkcom.GetP2PConnectionStatus() <= 0) {
                        KLog.i("test_开始连接");
                        CreatedP2PNetwork(path);
                    }else {
                        EventBus.getDefault().post(new MyStatus(qlinkcom.GetP2PConnectionStatus()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                KLog.i("CreatedP2PNetwork方法退出了。。。。。。。。。。。。。");
            }
        }).start();
    }

    /**
     * 获取sdk的连接状态
     *
     * @return
     */
    public static void getP2PConnnectStatus(final P2PCallBack p2PCallBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                KLog.i("开始获取p2pId");
                while (true) {
                    if (qlinkcom.GetP2PConnectionStatus() > 0) {
                        //int result = qlinkcom.DeleteFriendAll();
                        KLog.i("停止循环");
                        break;
                    }
                }
                byte[] myId = new byte[100];
                if (qlinkcom.ReturnOwnP2PId(myId) == 0) {
                    KLog.i(new String(myId).trim());
                    p2PCallBack.onResult(new String(myId).trim());
                }
            }
        }).start();
    }

    /**
     * 关闭p2p的连接
     *
     * @return
     */
    public static int endP2PConnenct() {
        return EndP2PConnection();
    }

    /**
     * 获取自己的p2p账号的id
     *
     * @return
     */
    public static int getOwnP2PId(byte[] myOwnp2pId) {
        return ReturnOwnP2PId(myOwnp2pId);
    }


    /**
     * Java_com_stratagile_qlink_qlinkcom_CreatedP2PNetwork()
     * * This is the entry of the p2p function, android app must call it firstly to use the p2p.
     * * The function will run with while(1) loop to complete the p2p function, so it is needed to open a thread to run this function.
     */
    public static native int CreatedP2PNetwork(String dataPath);

    /**
     * check if we conncected to the p2p Network
     * * -1 if qlinkNode is not valid
     * * 0 not connect
     * * 1 connected to p2p network, TCP
     * * 2 connected to p2p network, UDP
     * * call this function and update app own connected state on UI
     */
    @Deprecated
    public static native int GetP2PConnectionStatus();

    /**
     * End a p2p_network
     * * it shall be called when app quit or to stop the p2p network
     * 退出app时调用
     */
    public static native int EndP2PConnection();

    /**
     * Java_com_stratagile_qlink_qlinkcom_ReturnOwnP2PId
     * * call this function to get our own p2p ID,  p2p ID with lenth TOX_ADDRESS_SIZE (38 bytes)
     * * for example : 2EADC1764978270C0750374D1C1913226D84B41C652FE132AA8FBA3FEAC51D77C265812D4746
     * * but the parameter id is char *type, please make sure id with more than TOX_ADDRESS_SIZE*2 + 1 to save it
     * * when app got it own p2p id, call the blockchain sdk to save the SSID+MAC+P2PID+etc
     * * 0 got the ID
     * * -1 qlinkNode is not valid
     * * -2 if p2pid is not valid
     */
    public static native int ReturnOwnP2PId(byte[] myOwnp2pId);

    /**
     * Java_com_stratagile_qlink_qlinkcom_AddFriend
     * *
     * * the friend p2pid has the same strcture of its own p2pid, see the Java_com_stratagile_qlink_qlinkcom_ReturnOwnP2PId() for detail
     * * for example : 2EADC1764978270C0750374D1C1913226D84B41C652FE132AA8FBA3FEAC51D77C265812D4746
     * * After the app seached the local wifi, call the blockchain sdk with the parameters of wifi SSID+MAC and get the friend p2p ID
     * * Call this function to add p2p friend with the parameter of the friend p2pid
     * * And then the p2p function will try to monitor if it is ok to build a peer to peer connection with this friend
     * *
     * * -1 qlinkNode is not valid
     * * -2 invalid friendid address
     * * num is this location of friend in friend list, for example, if this is the 1st friend, num is 0, 2nd friend, num is 1.
     */
    public static native int AddFriend(String friendId);

    /** Java_com_stratagile_qlink_qlinkcom_DeleteFriendAll
     ** Delete All friend
     ** 0 success
     ** -1 qlinkNode not valid
     ** -2 delete fail
    */
    @Deprecated
    public static native int DeleteFriendAll();
    /**
     * Java_com_stratagile_qlink_qlinkcom_GetNumOfFriends()
     * * return the num of added friends, the app may use it to list or copy the friend list
     * * -1 qlinkNode is not valid
     * * >=0 friendnum
     */
    public static native int GetNumOfFriends();

    /**
     * Java_com_stratagile_qlink_qlinkcom_GetFriendP2PPublicKey
     * *
     * * input the friendnum ( 0 ~ (friendnum-1)) and get the pubKey of the friend
     * * Pubkey 32 bytes long which is just the former 32 bytes of the friend p2p ID
     * * 0 get the pubkey
     * * -1 qlinkNode is not valid
     * * -2 invalid input friend num
     * * -3 invalid pubKey address
     * 没什么用
     * 从 2018.04.12 开始有用了。
     */
    public static native int GetFriendP2PPublicKey(String p2pId, byte[] friendPubKey);

    /**
     * Java_com_stratagile_qlink_qlinkcom_GetFriendNumInFriendlist
     * * Input the friend ID and get the friend num back
     * * After the app get the friend p2p ID from the block chain, app may call this function the get the friendnum
     * * The friend num may be quite useful in the other function
     * * -1 qlinkNode is not valid
     * * -2 invalid input friendId
     * * -3 friend not in list
     * * >=0 the friend num
     * 获取该好友在自己好友列表中的位置
     */
    public static native int GetFriendNumInFriendlist(String p2pId);

    /**
     * Get friend connection status
     * * input the friendnum to get the status of the connection between app itself and the friend
     * * 0 not connected
     * * 1 tcp connected
     * * 2 udp connected
     * * android app must check this first before request the wifi password of the friend
     * * -1 qlinkNode not valid
     */
    @Deprecated
    public static native int GetFriendConnectionStatus(String friendNum);

    /**
     * Java_com_stratagile_qlink_qlinkcom_SaveWifiPassword
     * * save the wifi password, ssid, mac of the wifi owner
     * * android app shall call this function for wifi owner
     * * ssid and mac is not check right now, will add later
     * * 0 saved ok
     * * -1 qlinkNode not valid
     * * -2 password invalid
     * * -3/-4/-5 password file error
     * {
     * "WIFIINFO":	[{
     * "WIFINUM":	1,
     * "SSID":	"zhijiehome",
     * "MAC":	"00:0c:29:86:d9:94",
     * "PASSWORD":	"m8987",
     * "SAVETIME":	"2017-12-12 17:19:14"
     * }]
     * }
     */
    @Deprecated
    public static native int SaveWifiPassword(String password, String ssid_name, String mac_addr);

    /**
     * Java_com_stratagile_qlink_qlinkcom_SendWifiPasswordRequest
     * * Send request to get the wifi password of the friend
     * * the owner may have several wifi asset, so specific the ssid and mac
     * * ssid and mac is not check right now, will add later
     * * 0 saved ok
     * * -1 qlinkNode not valid
     */
    @Deprecated
    public static native int SendWifiPasswordRequest(int friendNum, String ssid_name, String mac_addr);

    /**
     * Java_com_stratagile_qlink_qlinkcom_GetWifiPassword
     * * after Java_com_stratagile_qlink_qlinkcom_SendWifiPasswordRequest, app could can this fucntion to get the wifi password
     * * the owner may have several wifi asset, so specific the ssid and mac
     * * ssid and mac is not check right now, will add later
     * * 0 Got the Password
     * * -1 qlinkNode not valid
     * * -1 qlinkNode not valid
     * * -2 Friend has no save Pass, don't need to wait
     * * -3 Friend still not response, can keep wait
     */
    @Deprecated
    public static native int GetWifiPassword(int friendNum, String ssid_name, String mac_addr, byte[] friendPubKey);

    /**
     * @param freindNumber 好友的编号
     * @param message      自己拼接的json，
     *                     {"Type":"wifibasicinfoReq","Data": "{ "SSID": "YYM-5", "MAC":"00:0c:29:86:d9:94"}"}
     * @return 返回消息发送成功或者失败
     * ** 0 SendRequest ok
     * * -1 qlinkNode not valid
     * * -2 message not valid
     * * -3 friend_not_valid
     */
    public static native int SendRequest(String freindNumber, String message);


    /**
     * 传送文件
     * 注意，这个方法是由需要我的头像的用户先发消息过来给我，然后我我再把我的头像发送过去
     *
     * @return
     */
    public static native int Addfilesender(String freindNum, String flieName);



    /* Java_com_stratagile_qlink_qlinkcom_CreatedNewGroupChat
    ** groupnum: Created success
    ** -1: qlinkNode not valid
    ** -2: Created fail
    * 创建群组聊天
    */
    public static native int CreatedNewGroupChat();

    /* Java_com_stratagile_qlink_qlinkcom_InviteFriendToGroupChat
    ** 0: Invite success
    ** -1: qlinkNode not valid
    ** -2: Invite fail
    ** -3 friendnum not valid
    * 邀请好友加入group接口
    */
    public static native int InviteFriendToGroupChat(String friendNum, int groupNum);


    /* Java_com_stratagile_qlink_qlinkcom_SendMessageToGroupChat
    ** 0: send message success
    ** -1: qlinkNode not valid
    ** -2: message is  null
    ** -3 send message fail
    * 发送消息到group接口
    */
    public static native int SendMessageToGroupChat(int groupNum, String message);

    /* Call_GroupChatMessage_Process_Func_To_Java
    * name 节点的名字，java不要管
    * 该方法由c层调用
    */
    public void CallGroupChatMessageProcess(String name, String message, int groupNum) {
        KLog.i("收到tox的group消息了。。");
        KLog.i(name);
        KLog.i(message);
        KLog.i("群组Id: " + groupNum);
        Gson gson = new Gson();
        Message message1 = gson.fromJson(message, Message.class);
        if (message1.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
            return;
        }
        message1.setGroupNum(groupNum);
        if (message1.getP2pId().equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
            message1.setDirection(1);
        } else {
            message1.setDirection(0);
        }
        if (ConstantValue.messageMap.get(groupNum) == null) {
            ConstantValue.messageMap.put(groupNum, new ArrayList<>());
        }
        ConstantValue.messageMap.get(groupNum).add(message1);
        EventBus.getDefault().post(message1);
    }

    /**
     * 加入群组成功的回调
     */
    public void CallGetGroupNumProcess(int groupnum) {
        KLog.i("加入群组成功，id：" + groupnum);
        EventBus.getDefault().post(new JoinNewGroup(groupnum));
    }



    /**
     * 自己的状态改变，由c调用
     * 0 无连接
     * 1 tcp
     * 2 udp
     */
    public void CallSelfChange(int status) {
        KLog.i("获取我自己的状态为:" + status);
        LogUtil.addLog("获取我自己的状态为" + status, getClass().getSimpleName());
        ConstantValue.myStatus = status;
        Qsdk.getInstance().handlerSelfStatusChange(status);
    }

    /**
     * @param friendNumber 好友的编号
     * @param status       状态
     *                     我发起一个WiFi连接的请求，把对方的p2pId传过去，会异步的返回到这个方法
     */
    public void CallFriendChange(String friendNumber, int status) {
        LogUtil.addLog("好友的编号为" + friendNumber + "好友的状态为:" + status, getClass().getSimpleName());
//        KLog.i("好友的编号为:" + friendNumber);
//        KLog.i("好友的状态为:" + status);
        Qsdk.getInstance().getFriendSharedVpnInfo(friendNumber, status);
//        Qsdk.getInstance().getFriendCurrentConnectWifiInfo(friendNumber, status);
        if (status > 0) {
            Qsdk.getInstance().handleUnReportRecord(friendNumber);
        }
    }

    /**
     * @param message      消息内容
     * @param friendNumber 好友编号
     */
    public void CallFriendMessageProcess(String message, String friendNumber) {
        KLog.i("好友传过来的消息为:" + message);
        LogUtil.addLog("收到的消息为：" + message, getClass().getSimpleName());
//        KLog.i("好友的编号为:" + friendNumber);
        Qsdk.getInstance().handlerFriendMessage(message, friendNumber);
    }

    public void ShowNativeLog(String message) {
        KLog.i("c语言中打印出来的log为:" + message);
    }

    public void CallFileMessageProcess(String filename, int filesize, String friendnum) {
//        ConstantValue.isLoadingImg = false;
//        KLog.i("c层的文件传输完毕:" + filename);
//        if (filename.contains(".jpg")) {
//            KLog.i("图片文件发送完毕");
//            List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
//            for (WifiEntity wifiEntity : wifiEntityList) {
//                if (wifiEntity.getFreindNum().equals(friendnum)) {
//                    wifiEntity.setIsLoadingAvater(false);
//                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
//                    break;
//                }
//            }
//        } else {
//            KLog.i("vpn文件发送完毕");
//            VpnSendEnd vpnSendEnd = new VpnSendEnd();
//            vpnSendEnd.setProfileLocalPath(filename);
//            EventBus.getDefault().post(vpnSendEnd);
//        }
    }

    /**
     * c层传过来了文件了。
     * @param pos
     * @param data  为文件的内容
     * @param length
     */
    public void SendFiledata(int pos,String data,int length) {
        KLog.i("c层的文件传输完毕");
        KLog.i("c层传过来的内容为：" + data);
        KLog.i(pos);
        KLog.i(length);
    }

    public String GetFilePath(String oldFilePath) {
        KLog.i(oldFilePath);
        String newPath = Environment.getExternalStorageDirectory() + "/Qlink/vpn";
        String fileName = oldFilePath.substring(oldFilePath.lastIndexOf("/"), oldFilePath.length());
        KLog.i(fileName);
        newPath = newPath + fileName;
        KLog.i(newPath);
        return newPath;
    }



    private String CallJsonFromTox() {
        KLog.i("开始获取tox的json数据");
        String toxJsonResult = "";
        toxJsonResult = FileUtil.getJson(AppConfig.getInstance(), "tox.json");
//        KLog.i(toxJsonResult);
        String jsonPath = Environment.getExternalStorageDirectory() + "/Qlink/Profile/jsonFile.json";
        File jsonFile = new File(jsonPath);
        if (jsonFile.exists()) {
            try {
                // 建立一个输入流对象reader
                InputStreamReader reader = new InputStreamReader(new FileInputStream(jsonFile));
                // 建立一个对象，它把文件内容转成计算机能读懂的语言
                BufferedReader br = new BufferedReader(reader);
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                line = br.readLine();
                stringBuffer.append(line);
                while (line != null) {
                    // 一次读入一行数据
                    line = br.readLine();
                    stringBuffer.append(line);
                }
                if (stringBuffer.length() >= 100) {
//                    KLog.i(stringBuffer.toString());
                    toxJsonResult = stringBuffer.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        KLog.i(toxJsonResult);
        return toxJsonResult;
    }
    /* Java_com_stratagile_qlink_qlinkcom_Encrypt
       ** encode string
       ** return data_encrypt string success
       ** -1: qlinkNode not valid
       ** -2,-3: encrypt_string is null
       ** -4 malloc fail
       ** -5 public_key_a is null
       * 加密接口
       */
    public static native String Encrypt(String source);

    /* Java_com_stratagile_qlink_qlinkcom_Decrypt
       ** decode string
       ** return data_encrypt string success
       ** -1: qlinkNode not valid
       ** -2,-3: encrypt_string is null
       ** -4 malloc fail
       ** -5 public_key_a is null
       **解密接口
       */
    public static native String Decrypt(String source);

    /* Java_com_stratagile_qlink_qlinkcom_free
       ** free
       **释放内存
       */
    public static native void free(String source);

    /* Java_com_stratagile_qlink_qlinkcom_AES
      ** free
      **释放内存
      */

    /**
     * 加密解密接口
     * @param bytes
     * @param mode 0 加密   1 解密
     */
    public static native byte[] AES(byte[] bytes,int mode);
}
