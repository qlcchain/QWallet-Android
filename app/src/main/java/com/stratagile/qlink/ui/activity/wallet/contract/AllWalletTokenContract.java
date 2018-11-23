package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for AllWalletTokenActivity
 * @Description: $description
 * @date 2018/10/24 15:49:29
 */
public interface AllWalletTokenContract {
    interface View extends BaseView<AllWalletTokenContractPresenter> {
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

        void getCameraPermissionSuccess();
    }

    interface AllWalletTokenContractPresenter extends BasePresenter {
        void getETHWalletDetail(String address, Map map);

        void getNeoWalletDetail(String address, Map map);

        void getToeknPrice(ArrayList<TokenInfo> arrayList, HashMap map);

        void getPermission();
    }
}