package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EosImportActivity
 * @Description: $description
 * @date 2018/11/26 17:06:38
 */
public interface EosImportContract {
    interface View extends BaseView<EosImportContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void accountInfoBack(EosAccountInfo eosAccountInfo);

        void getEosKeyAccountBack(ArrayList<EosKeyAccount> eosKeyAccounts);

        void reportCreatedWalletSuccess();
    }

    interface EosImportContractPresenter extends BasePresenter {
        /**
         *
         */
        void getEosAccountInfo(Map map);

        void reportWalletCreated(String address, String publicKey, String privateKey);
    }
}