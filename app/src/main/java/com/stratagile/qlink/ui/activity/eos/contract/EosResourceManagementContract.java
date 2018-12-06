package com.stratagile.qlink.ui.activity.eos.contract;

import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for EosResourceManagementActivity
 * @Description: $description
 * @date 2018/12/04 18:08:32
 */
public interface EosResourceManagementContract {
    interface View extends BaseView<EosResourceManagementContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setEosResource(EosResource eosResource);

        void getTokenPriceBack(ArrayList<TokenInfo> tokenInfos);
    }

    interface EosResourceManagementContractPresenter extends BasePresenter {
        /**
         *
         */
        void getEosResource(Map map);

        void getEosWalletDetail(String address, Map map);
    }
}