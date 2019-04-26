package com.stratagile.qlink.ui.activity.finance.contract;

import com.stratagile.qlink.entity.finance.HistoryRecord;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for HistoryRecordActivity
 * @Description: $description
 * @date 2019/04/24 13:48:39
 */
public interface HistoryRecordContract {
    interface View extends BaseView<HistoryRecordContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void setData(HistoryRecord historyRecord);
    }

    interface HistoryRecordContractPresenter extends BasePresenter {
        void getHistory(Map map);
    }
}