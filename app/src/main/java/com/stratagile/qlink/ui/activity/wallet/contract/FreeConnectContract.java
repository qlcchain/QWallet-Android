package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for FreeConnectActivity
 * @Description: $description
 * @date 2018/07/18 11:53:01
 */
public interface FreeConnectContract {
    interface View extends BaseView<FreeConnectContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void onGetFreeNumBack(int num);

        void onGetFreeRecordBack(FreeRecord freeRecord);
    }

    interface FreeConnectContractPresenter extends BasePresenter {
//        /**
//         *
//         */
        void zsFreeNum(Map map);

        void queryFreeRecords(Map map);
    }
}