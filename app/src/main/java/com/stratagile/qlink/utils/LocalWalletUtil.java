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
import java.util.List;

public class LocalWalletUtil {
    public static void updateNeoWallet() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList.size() != 0) {
            Gson gson = new Gson();
            String saveData = gson.toJson(walletList);
            saveData = ETHWalletUtils.enCodePassword(saveData);
            FileUtil.savaData("/Qlink/neoWallet", saveData);
        }
    }

    public static void getLocalNeoWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qlink/neoWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.getPassword(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<Wallet> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<MyAsset>>() {}.getType());

        } else {

        }
    }

    public static void updateEthWallet() {
        List<EthWallet> walletList = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        if (walletList.size() != 0) {
            Gson gson = new Gson();
            String saveData = gson.toJson(walletList);
            saveData = ETHWalletUtils.enCodePassword(saveData);
            FileUtil.savaData("/Qlink/ethWallet", saveData);
        }
    }

    public static void getLocalEthWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qlink/ethWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.getPassword(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<EthWallet> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<EthWallet>>() {}.getType());

        } else {

        }
    }
}
