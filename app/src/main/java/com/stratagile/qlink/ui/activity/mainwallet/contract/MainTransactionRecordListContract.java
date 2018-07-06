package com.stratagile.qlink.ui.activity.mainwallet.contract;

import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author zl
 * @Package The contract for MainTransactionRecordFragment
 * @Description: $description
 * @date 2018/06/13 20:52:12
 */
public interface MainTransactionRecordListContract {
    interface View extends BaseView<MainTransactionRecordListContractPresenter> {
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

    interface MainTransactionRecordListContractPresenter extends BasePresenter {

        void getMainTransactionRecordListFromServer(String mainTransactionRecordStatus,int requestPage,int onePageSize);

        void getRecords(Map map);
    }
}