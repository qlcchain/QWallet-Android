package com.stratagile.qlink.ui.activity.wallet.contract;

import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for TransactionRecordActivity
 * @Description: $description
 * @date 2018/01/26 17:16:57
 */
public interface TransactionRecordContract {
    interface View extends BaseView<TransactionRecordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void getRecordSuccess(Record record);


    }

    interface TransactionRecordContractPresenter extends BasePresenter {
//        /**
//         *
//         */
//        void getBusinessInfo(Map map);

        void getRecords(Map map);
    }
}