package com.stratagile.qlink.ui.activity.neo.contract;

import com.stratagile.qlink.api.transaction.SendCallBack;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for NeoTransferActivity
 * @Description: $description
 * @date 2018/11/06 18:16:07
 */
public interface NeoTransferContract {
    interface View extends BaseView<NeoTransferContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getTokenPriceBack(ArrayList<TokenInfo> arrayList);

        void sendSuccess(String s);
    }

    interface NeoTransferContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getNeoWalletDetail(String address, Map map);

        void getUtxo(String address, SendCallBack sendCallBack);

        void sendNEP5Token(TokenInfo tokenInfo, String amount, String toAddress, String remark);

        void sendNeo(String amount, String toAddress, TokenInfo tokenInfo);
    }
}