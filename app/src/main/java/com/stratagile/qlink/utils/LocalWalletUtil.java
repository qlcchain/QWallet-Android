package com.stratagile.qlink.utils;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalWalletUtil {
    public static void updateLocalQlcWallet() {
        List<QLCAccount> walletList = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
        if (walletList != null && walletList.size() > 0) {
            Gson gson = new Gson();
            String saveData = gson.toJson(walletList);
            saveData = ETHWalletUtils.encryption(saveData);
            FileUtil.savaData("/Qwallet/qlcWallet", saveData);
        }
    }

    public static ArrayList<QLCAccount> getLocalQlcWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qwallet/qlcWallet");
        if (file.exists()) {
            String qlcWalletStr = FileUtil.getStrDataFromFile(file);
            qlcWalletStr = ETHWalletUtils.decrypt(qlcWalletStr);
            KLog.i(qlcWalletStr);
            Gson gson = new Gson();
            ArrayList<QLCAccount> wallets = gson.fromJson(qlcWalletStr, new TypeToken<ArrayList<QLCAccount>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }
    public static void updateLocalNeoWallet() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList != null && walletList.size() > 0) {
            Gson gson = new Gson();
            String saveData = gson.toJson(walletList);
            saveData = ETHWalletUtils.encryption(saveData);
            FileUtil.savaData("/Qwallet/neoWallet", saveData);
        }
    }

    public static ArrayList<Wallet> getLocalNeoWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qwallet/neoWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.decrypt(neoWalletStr);
            KLog.i(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<Wallet> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<Wallet>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }

    public static void updateLocalEthWallet() {
        List<EthWallet> walletList = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        if (walletList != null && walletList.size() > 0) {
            Gson gson = new Gson();
            String saveData = gson.toJson(walletList);
            saveData = ETHWalletUtils.encryption(saveData);
            FileUtil.savaData("/Qwallet/ethWallet", saveData);
        }
    }

    public static void updateLocalEosWallet() {
        List<EosAccount> walletList = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        if (walletList != null && walletList.size() > 0) {
            Gson gson = new Gson();
            String saveData = gson.toJson(walletList);
            saveData = ETHWalletUtils.encryption(saveData);
            FileUtil.savaData("/Qwallet/eosWallet", saveData);
        }
    }

    public static void initGreenDaoFromLocal() {
        if (AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll().size() == 0) {
            if (getLocalEthWallet() != null && getLocalEthWallet().size() != 0) {
                AppConfig.getInstance().getDaoSession().getEthWalletDao().insertInTx(getLocalEthWallet());
            }
        }
        if (AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().size() == 0) {
            if (getLocalNeoWallet() != null && getLocalNeoWallet().size() != 0) {
                AppConfig.getInstance().getDaoSession().getWalletDao().insertInTx(getLocalNeoWallet());
            }
        }
        if (AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll().size() == 0) {
            if (getLocalEosWallet() != null && getLocalEosWallet().size() != 0) {
                AppConfig.getInstance().getDaoSession().getEosAccountDao().insertInTx(getLocalEosWallet());
            }
        }
        if (AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll().size() == 0) {
            if (getLocalQlcWallet() != null && getLocalQlcWallet().size() != 0) {
                AppConfig.getInstance().getDaoSession().getQLCAccountDao().insertInTx(getLocalQlcWallet());
            }
        }
    }

    public static ArrayList<EthWallet> getLocalEthWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qwallet/ethWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.decrypt(neoWalletStr);
            KLog.i(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<EthWallet> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<EthWallet>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }
    public static ArrayList<EosAccount> getLocalEosWallet() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qwallet/eosWallet");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            neoWalletStr = ETHWalletUtils.decrypt(neoWalletStr);
            Gson gson = new Gson();
            ArrayList<EosAccount> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<EosAccount>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }

    public static ArrayList<String> getLocalTokens() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qwallet/tokens");
        if (file.exists()) {
            String neoWalletStr = FileUtil.getStrDataFromFile(file);
            Gson gson = new Gson();
            ArrayList<String> wallets = gson.fromJson(neoWalletStr, new TypeToken<ArrayList<String>>() {
            }.getType());
            return wallets;
        } else {
            return null;
        }
    }
    public static void updateLocalTokens(ArrayList<String> list) {
        Gson gson = new Gson();
        String saveData = gson.toJson(list);
        FileUtil.savaData("/Qwallet/tokens", saveData);
    }

}
