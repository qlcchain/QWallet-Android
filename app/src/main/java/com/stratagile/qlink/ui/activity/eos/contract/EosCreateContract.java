package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.eos.EosNeedInfo;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EosCreateActivity
 * @Description: $description
 * @date 2018/12/07 11:29:04
 */
public interface EosCreateContract {
    interface View extends BaseView<EosCreateContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setEth(TokenInfo tokenInfo);

        void transferEthSuccess(String hash);

        void createEosAccountSuccess(String s);

        void accountInfoBack(EosAccountInfo eosAccountInfo, int flag);

        void setEosNeedInfo(EosNeedInfo eosNeedInfo);

        void getEosKeyAccountBack(ArrayList<EosKeyAccount> eosKeyAccounts);
    }

    interface EosCreateContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getETHWalletDetail(String address, Map map);

        void transactionEth(TokenInfo tokenInfo, String toAddress, String amount, int limit, int price);

        void createEosAccount(Map map);

        void getEosAccountInfo(Map map, int flag);

        void getEosNeedInfo(Map map);

        void getEosKeyAccount(Map map);

        void reportWalletCreated(String address, String publicKey, String privateKey);
    }
}