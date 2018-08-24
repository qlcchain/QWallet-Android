package com.stratagile.qlink.ui.activity.file.contract;

import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.Map;

/**
 * @author hzp
 * @Package The contract for FileChooseActivity
 * @Description: $description
 * @date 2018/05/18 14:15:35
 */
public interface FileChooseContract {
    interface View extends BaseView<FileInfosContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();
    }

    interface FileInfosContractPresenter extends BasePresenter {
        /**
         *
         */
//        void loadFileInfos();
    }
}