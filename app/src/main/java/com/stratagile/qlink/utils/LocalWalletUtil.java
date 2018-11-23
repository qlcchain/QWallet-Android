package com.stratagile.qlink.utils;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalWalletUtil {
    public static void updateNeoWallet() {
//        ArrayList<Wallet> wallets = getLocalNeoWallet();
//        Map<String, Wallet> walletMap = new HashMap<>();
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
//        if (wallets.size() != 0) {
//            for (Wallet wallet : wallets) {
//                walletMap.put(wallet.getAddress(), wallet);
//            }
//        }
//        if (walletList.size() != 0) {
//            for (Wallet wallet : walletList) {
//                walletMap.put(wallet.getAddress(), wallet);
//            }
//        }
//        ArrayList<Wallet> ethWalletArrayList = new ArrayList<>(walletMap.values());
        Gson gson = new Gson();
        String saveData = gson.toJson(walletList);
        saveData = ETHWalletUtils.enCodePassword(saveData);
        FileUtil.savaData("/Qlink/neoWallet", saveData);
    }

    public static ArrayList<Wallet> getLocalNeoWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qlink/neoWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.getPassword(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<Wallet> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<Wallet>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }

    public static void updateLocalEthWallet() {
//        ArrayList<EthWallet> ethWallets = getLocalEthWallet();
//        Map<String, EthWallet> ethWalletMap = new HashMap<>();
        List<EthWallet> walletList = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
//        if (ethWallets.size() != 0) {
//            for (EthWallet ethWallet : ethWallets) {
//                ethWalletMap.put(ethWallet.getAddress(), ethWallet);
//            }
//        }
//        if (walletList.size() != 0) {
//            for (EthWallet ethWallet : walletList) {
//                ethWalletMap.put(ethWallet.getAddress(), ethWallet);
//            }
//        }
//        ArrayList<EthWallet> ethWalletArrayList = new ArrayList<>(ethWalletMap.values());
        Gson gson = new Gson();
        String saveData = gson.toJson(walletList);
        saveData = ETHWalletUtils.enCodePassword(saveData);
        FileUtil.savaData("/Qlink/ethWallet", saveData);
    }

    public static void initGreenDaoFromLocal() {
        if (AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll().size() == 0) {
            AppConfig.getInstance().getDaoSession().getEthWalletDao().insertInTx(getLocalEthWallet());
        }
        if (AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().size() == 0) {
            AppConfig.getInstance().getDaoSession().getWalletDao().insertInTx(getLocalNeoWallet());
        }
    }

    public static ArrayList<EthWallet> getLocalEthWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qlink/ethWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.getPassword(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<EthWallet> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<EthWallet>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }
}
