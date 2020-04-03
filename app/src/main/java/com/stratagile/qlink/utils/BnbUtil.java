package com.stratagile.qlink.utils;

import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.db.BnbWallet;
import com.stratagile.qlink.db.EthWallet;

import java.util.List;

public class BnbUtil {
    public static String generateNewBnbWalletName() {
        List<BnbWallet> bnbWallets = AppConfig.getInstance().getDaoSession().getBnbWalletDao().loadAll();
        int size = bnbWallets.size();
        if (size < 9) {
            return "BNB-Wallet 0" + (bnbWallets.size() + 1);
        } else {
            return "BNB-Wallet " + (bnbWallets.size() + 1);
        }
    }

    public static String convertMnemonicList(List<String> mnemonics) {
        StringBuilder sb = new StringBuilder();
        for (String mnemonic : mnemonics
        ) {
            sb.append(mnemonic);
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
