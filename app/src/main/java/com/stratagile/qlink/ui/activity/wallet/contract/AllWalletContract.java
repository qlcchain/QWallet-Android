package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.ClaimData;
import com.stratagile.qlink.entity.NeoTransfer;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.WinqGasBack;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for AllWalletFragment
 * @Description: $description
 * @date 2018/10/24 10:17:57
 */
public interface AllWalletContract {
    interface View extends BaseView<AllWalletContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getNeoWalletInfoBack(ArrayList<TokenInfo> tokenInfos);
        void getTokenPriceBack(ArrayList<TokenInfo> tokenInfos);

        void getWinqGasBack(Balance balance);

        void queryWinqGasBack(WinqGasBack winqGasBack);

        void gotWinqGasSuccess(String s);

        void queryGasClaimBack(ClaimData claimData);

        void claimGasBack(NeoTransfer baseBack);
    }

    interface AllWalletContractPresenter extends BasePresenter {
        void getETHWalletDetail(String address, Map map);

        void getNeoWalletDetail(String address, Map map);

        void getToeknPrice(ArrayList<TokenInfo> arrayList, HashMap map);

        /**
         * 获取winqgas，也就是获取测试网的qlc
         * @param address
         */
        void getWinqGas(String address);

        /**
         * 查询可以领取的winqgas
         * @param map
         */
        void queryWinqGas(Map map);

        /**
         * 领取winqGas
         * @param map
         */
        void gotWinqGas(Map map);

        void getNeoGasClaim(Map map);

        void claimGas(String address, String amount, String txid);
    }
}