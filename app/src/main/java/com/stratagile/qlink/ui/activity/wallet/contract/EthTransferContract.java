package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EthTransferActivity
 * @Description: $description
 * @date 2018/10/31 10:17:17
 */
public interface EthTransferContract {
    interface View extends BaseView<EthTransferContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getEthWalletBack(EthWalletInfo ethWalletInfo);

        void getTokenPriceBack(TokenPrice tokenPrice);
    }

    interface EthTransferContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getETHWalletDetail(String address, Map map);

        void getToeknPrice(Map map);

        void transaction(TokenInfo tokenInfo, String toAddress, String amount, int limit, int price);
    }
}