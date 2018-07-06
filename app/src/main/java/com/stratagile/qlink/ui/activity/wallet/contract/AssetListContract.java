package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for AssetFragment
 * @Description: $description
 * @date 2018/01/18 20:42:28
 */
public interface AssetListContract {
    interface View extends BaseView<AssetListContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getAssetSuccess();
    }

    interface AssetListContractPresenter extends BasePresenter {

        void getAssetListFromServer(String assetStatus,int requestPage,int onePageSize);
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getAssetInfoFromServer(Map map);
    }
}