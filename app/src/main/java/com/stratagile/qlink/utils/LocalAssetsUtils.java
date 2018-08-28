package com.stratagile.qlink.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.VpnEntityDao;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.db.WifiEntityDao;
import com.stratagile.qlink.entity.MyAsset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2018/3/22.
 */

public class LocalAssetsUtils {

    private static ArrayList<Map> vpnList = new ArrayList();

    public static ArrayList<Map> getVpnList() {
        return vpnList;
    }

    public static void setVpnList(ArrayList<Map> vpnList) {
        LocalAssetsUtils.vpnList = vpnList;
    }

    /**
     * 同步sd上的资产数据到greenDao
     */
    public static void updateGreanDaoFromLocal() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList != null && walletList.size() != 0) {
            //wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
            List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            for (Wallet wallet : walletList) {
                Gson gson = new Gson();
                ArrayList<MyAsset> localAssetArrayList;
                try {
                    //开始读取sd卡的资产数据
                    String assetStr = "";
                    if (wallet != null) {
                        assetStr = FileUtil.readAssetsData(wallet.getAddress());
//                        KLog.i("开始同步本地资产" + assetStr);
                    }
                    if (!assetStr.equals("")) {
                        localAssetArrayList = gson.fromJson(assetStr, new TypeToken<ArrayList<MyAsset>>() {
                        }.getType());
                        for (MyAsset myAsset : localAssetArrayList) {

                            if (myAsset.getType() == 0)//wifi
                            {
                                for (WifiEntity wifiEntity : wifiEntityList) {

                                    if (myAsset.getWifiEntity().getSsid().equals(wifiEntity.getSsid())) {
                                        myAsset.getWifiEntity().setId(wifiEntity.getId());//这个很重要，要不没法更新greenDao
                                        myAsset.getWifiEntity().setIsConnected(wifiEntity.getIsConnected());
                                        myAsset.getWifiEntity().setOnline(wifiEntity.getOnline());
                                        myAsset.getWifiEntity().setIsLoadingAvater(wifiEntity.getIsLoadingAvater());
                                        myAsset.getWifiEntity().setAvaterUpdateTime(wifiEntity.getAvaterUpdateTime());
                                        myAsset.getWifiEntity().setUnReadMessageCount(wifiEntity.getUnReadMessageCount());
                                        myAsset.getWifiEntity().setGroupNum(wifiEntity.getGroupNum());
                                        myAsset.getWifiEntity().setAssetTranfer(wifiEntity.getAssetTranfer());
                                        myAsset.getWifiEntity().setFreindNum(wifiEntity.getFreindNum());
                                        myAsset.getWifiEntity().setRegisterQlc(wifiEntity.getRegisterQlc());
                                        //添加了抢注册功能，p2pId和钱包地址可能会变化
                                        myAsset.getWifiEntity().setOwnerP2PId(wifiEntity.getOwnerP2PId());
                                        myAsset.getWifiEntity().setWalletAddress(wifiEntity.getWalletAddress());
                                        myAsset.getWifiEntity().setIsRegiste(wifiEntity.getIsRegiste());
                                        myAsset.getWifiEntity().setAvatar(wifiEntity.getAvatar());
                                        myAsset.getWifiEntity().setWifiPassword(wifiEntity.getWifiPassword());
                                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(myAsset.getWifiEntity());
                                    }
                                }
                            } else if (myAsset.getType() == 1)//vpn
                            {
                                for (VpnEntity vpnEntity : vpnEntityList) {

                                    if (vpnEntity.getVpnName().equals(myAsset.getVpnEntity().getVpnName()) && vpnEntity.getIsMainNet() == myAsset.getVpnEntity().getIsMainNet()) {
                                        myAsset.getVpnEntity().setId(vpnEntity.getId());//这个很重要，要不没法更新greenDao
                                        myAsset.getVpnEntity().setIsConnected(vpnEntity.getIsConnected());
                                        myAsset.getVpnEntity().setOnline(vpnEntity.getOnline());
                                        myAsset.getVpnEntity().setIsLoadingAvater(vpnEntity.getIsLoadingAvater());
                                        myAsset.getVpnEntity().setAvaterUpdateTime(vpnEntity.getAvaterUpdateTime());
                                        myAsset.getVpnEntity().setUnReadMessageCount(vpnEntity.getUnReadMessageCount());
                                        myAsset.getVpnEntity().setGroupNum(vpnEntity.getGroupNum());
                                        myAsset.getVpnEntity().setAssetTranfer(vpnEntity.getAssetTranfer());
                                        myAsset.getVpnEntity().setFriendNum(vpnEntity.getFriendNum());
                                        myAsset.getVpnEntity().setAvatar(vpnEntity.getAvatar());
                                        myAsset.getVpnEntity().setCountry(vpnEntity.getCountry());
                                        myAsset.getVpnEntity().setRegisterQlc(vpnEntity.getRegisterQlc());
                                        myAsset.getVpnEntity().setConnsuccessNum(vpnEntity.getConnsuccessNum());
                                        //添加了抢注册功能，p2pId和钱包地址可能会变化
                                        myAsset.getVpnEntity().setP2pId(vpnEntity.getP2pId());
                                        if (vpnEntity.getP2pIdPc() != null && !vpnEntity.getP2pIdPc().equals("")) {
                                            myAsset.getVpnEntity().setP2pIdPc(vpnEntity.getP2pIdPc());
                                        }
                                        myAsset.getVpnEntity().setAddress(vpnEntity.getAddress());
                                        if (vpnEntity.getUsername() == null || vpnEntity.getUsername().equals("")) {

                                        } else {
                                            myAsset.getVpnEntity().setUsername(vpnEntity.getUsername());
                                        }
                                        if (vpnEntity.getPassword() == null || vpnEntity.getPassword().equals("")) {

                                        } else {
                                            myAsset.getVpnEntity().setPassword(vpnEntity.getPassword());
                                        }
                                        if (vpnEntity.getPrivateKeyPassword() == null || vpnEntity.getPrivateKeyPassword().equals("")) {

                                        } else {
                                            myAsset.getVpnEntity().setPrivateKeyPassword(vpnEntity.getPrivateKeyPassword());
                                        }
                                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(myAsset.getVpnEntity());


//                                        Map<String, Object> infoMap = new HashMap<>();
//                                        infoMap.put("vpnName", vpnEntity.getVpnName());
//                                        infoMap.put("p2pId", vpnEntity.getP2pId());
//                                        infoMap.put("country", vpnEntity.getCountry());
//                                        infoMap.put("continent", vpnEntity.getContinent());
//                                        infoMap.put("connectMaxnumber", vpnEntity.getConnectMaxnumber());
//                                        infoMap.put("profileLocalPath", vpnEntity.getProfileLocalPath());
//                                        infoMap.put("bandwidth", vpnEntity.getBandwidth());
//                                        infoMap.put("currentConnect", vpnEntity.getCurrentConnect());
//                                        infoMap.put("avaterUpdateTime", vpnEntity.getAvaterUpdateTime());
//                                        infoMap.put("qlc", vpnEntity.getQlc());
//                                        infoMap.put("exist", true);
//                                        infoMap.put("ipV4Address", vpnEntity.getIpV4Address());
//                                        infoMap.put("username", vpnEntity.getUsername());
//                                        infoMap.put("password", vpnEntity.getPassword());
//                                        infoMap.put("privateKeyPassword", vpnEntity.getPrivateKeyPassword());
//                                        vpnList.add(infoMap);
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {

                } finally {

                }
            }
        }else{
            String allWalletNames = FileUtil.getAllWalletNames();
            if(!"".equals(allWalletNames))
            {
                String[] allWalletNamesArray = allWalletNames.split(",");
                for (int i=0; i<allWalletNamesArray.length; i++) {
                    Gson gson = new Gson();
                    ArrayList<MyAsset> localAssetArrayList;
                    try {
                        //开始读取sd卡的资产数据
                        String assetStr = "";
                        assetStr = FileUtil.readAssetsData(allWalletNamesArray[i]);
                        if (!assetStr.equals("")) {
                            localAssetArrayList = gson.fromJson(assetStr, new TypeToken<ArrayList<MyAsset>>() {
                            }.getType());
                            for (MyAsset myAsset : localAssetArrayList) {

                                if (myAsset.getType() == 0)//wifi
                                {
                                    List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().where(WifiEntityDao.Properties.Ssid.eq(myAsset.getWifiEntity().getSsid())).list();
                                    if(wifiEntityList != null && wifiEntityList.size() == 0)
                                    {
                                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().insert(myAsset.getWifiEntity());
                                    }
                                } else if (myAsset.getType() == 1)//vpn
                                {
                                   /* VpnEntityDao vpnEntityDao = AppConfig.getInstance().getDaoSession().getVpnEntityDao();
                                    boolean isMainNet = myAsset.getVpnEntity().getIsMainNet();
                                    List<VpnEntity> VpnEntityList = vpnEntityDao.queryBuilder().where(VpnEntityDao.Properties.VpnName.eq(myAsset.getVpnEntity().getVpnName())).list();
                                    if(VpnEntityList != null && VpnEntityList.size() == 0)
                                    {
                                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(myAsset.getVpnEntity());
                                    }*/
                                    myAsset.getVpnEntity().setId(null);
                                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(myAsset.getVpnEntity());
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }

        }
    }

    /**
     * 新增本地资产数据
     *
     * @param myAsset
     */
    public static void insertLocalAssets(MyAsset myAsset) {
        if (myAsset == null) {
            return;
        }
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet;
        if (walletList != null && walletList.size() != 0) {
            wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
        } else {
            return;
        }
        Gson gson = new Gson();
        ArrayList<MyAsset> localAssetArrayList;
        try {
            //开始读取sd卡的资产数据
            String assetStr = "";
            if (wallet != null) {
                assetStr = FileUtil.readAssetsData(wallet.getAddress());
            }
            if (!assetStr.equals("")) {
                localAssetArrayList = gson.fromJson(assetStr, new TypeToken<ArrayList<MyAsset>>() {
                }.getType());
                boolean isHad = false;
                for (MyAsset myAssetItem : localAssetArrayList) {

                    if (myAsset.getType() == 0)//wifi
                    {
                        if (myAssetItem.getWifiEntity() != null && myAsset.getWifiEntity().getSsid().equals(myAssetItem.getWifiEntity().getSsid()) ) {
                            isHad = true;
                            break;
                        }
                    } else if (myAsset.getType() == 1)//vpn
                    {
                        if (myAssetItem.getVpnEntity() != null && myAsset.getVpnEntity().getVpnName().equals(myAssetItem.getVpnEntity().getVpnName()) && myAsset.getVpnEntity().getIsMainNet() == myAssetItem.getVpnEntity().getIsMainNet()) {
                            isHad = true;
                            break;
                        }
                    }
                }
                if(isHad)
                {
                    LocalAssetsUtils.updateLocalAssets(myAsset);
                }else{
                    localAssetArrayList.add(myAsset);
                    FileUtil.saveAssetsData(wallet.getAddress(), gson.toJson(localAssetArrayList));
                }


            } else {
                localAssetArrayList = new ArrayList<>();
                localAssetArrayList.add(myAsset);
                FileUtil.saveAssetsData(wallet.getAddress(), gson.toJson(localAssetArrayList));
            }

        } catch (Exception e) {

        } finally {

        }
    }

    /**
     * 批量更新
     *
     * @param myAssets
     */
    public static void updateList(ArrayList<MyAsset> myAssets) {
        if (myAssets == null) {
            return;
        }
        ArrayList<MyAsset> newList = new ArrayList();     //创建新集合
        Iterator it = myAssets.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            MyAsset obj = (MyAsset) it.next();       //记录每一个元素
            if (!newList.contains(obj)) {      //如果新集合中不包含旧集合中的元素
                newList.add(obj);       //将元素添加
            }
        }
        myAssets = newList;
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet;
        if (walletList != null && walletList.size() != 0) {
            wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
        } else {
            return;
        }
        Gson gson = new Gson();
        try {
            Iterator itadd = myAssets.iterator();        //根据传入的集合(旧集合)获取迭代器
            while (itadd.hasNext()) {          //遍历老集合
                MyAsset obj = (MyAsset) itadd.next();
                insertLocalAssets(obj);
            }
            //FileUtil.saveAssetsData(wallet.getAddress(), gson.toJson(myAssets));
        } catch (Exception e) {

        } finally {

        }
    }

    /**
     * 更新某个本地资产
     *
     * @param asset
     */
    public static void updateLocalAssets(MyAsset asset) {
        if (asset == null) {
            return;
        }
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet;
        if (walletList != null && walletList.size() != 0) {
            wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
        } else {
            return;
        }
        Gson gson = new Gson();
        ArrayList<MyAsset> localAssetArrayList;
        ArrayList<MyAsset> newAssetArrayList = new ArrayList<>();
        try {
            //开始读取sd卡的资产数据
            String assetStr = "";
            if (wallet != null) {
                assetStr = FileUtil.readAssetsData(wallet.getAddress());
            }
            if (!assetStr.equals("")) {
                localAssetArrayList = gson.fromJson(assetStr, new TypeToken<ArrayList<MyAsset>>() {
                }.getType());
                for (MyAsset myAsset : localAssetArrayList) {

                    if (myAsset.getType() == 0)//wifi
                    {
                        if (asset.getWifiEntity() != null && myAsset.getWifiEntity().getSsid().equals(asset.getWifiEntity().getSsid())) {
                            newAssetArrayList.add(asset);
                        } else {
                            newAssetArrayList.add(myAsset);
                        }
                    } else if (myAsset.getType() == 1)//vpn
                    {
                        if (asset.getVpnEntity() != null && myAsset.getVpnEntity().getVpnName().equals(asset.getVpnEntity().getVpnName()) && myAsset.getVpnEntity().getIsMainNet() == asset.getVpnEntity().getIsMainNet()) {
                            newAssetArrayList.add(asset);
                        } else {
                            newAssetArrayList.add(myAsset);
                        }
                    }
                }
                FileUtil.saveAssetsData(wallet.getAddress(), gson.toJson(newAssetArrayList));
            }

        } catch (Exception e) {

        } finally {

        }
    }
    /**
     * 获取全部本地资产
     * @return
     */
    public static ArrayList<MyAsset> getLocalAssetsList() {
        ArrayList<MyAsset> localAssetArrayList = new ArrayList<>();
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet;
        if (walletList != null && walletList.size() != 0) {
            wallet = walletList.get(SpUtil.getInt(AppConfig.getInstance(), ConstantValue.currentWallet, 0));
        } else {
            return localAssetArrayList;
        }
        Gson gson = new Gson();

        try {
            //开始读取sd卡的资产数据
            String assetStr = "";
            if (wallet != null) {
                assetStr = FileUtil.readAssetsData(wallet.getAddress());
            }
            if (!assetStr.equals("")) {
                localAssetArrayList = gson.fromJson(assetStr, new TypeToken<ArrayList<MyAsset>>() {
                }.getType());
            }

        } catch (Exception e) {

        } finally {

        }

        Iterator it = localAssetArrayList.iterator();        //根据传入的集合(旧集合)获取迭代器
        while (it.hasNext()) {          //遍历老集合
            MyAsset obj = (MyAsset) it.next();       //记录每一个元素
            if (obj.getType() == 1 &&  !VpnUtil.isInSameNet(obj.getVpnEntity())) {
                it.remove();
            }
        }
        return localAssetArrayList;
    }
}
