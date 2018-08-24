package com.stratagile.qlink.ui.activity.file.contract;

import com.stratagile.qlink.data.fileInfo.FileInfo;
import com.stratagile.qlink.data.fileInfo.FileInfosRepository;
import com.stratagile.qlink.ui.activity.base.BasePresenter;
import com.stratagile.qlink.ui.activity.base.BaseView;

import java.util.List;
import java.util.Map;

/**
 * @author hzp
 * @Package The contract for FileInfosFragment
 * @Description: $description
 * @date 2018/05/18 16:46:15
 */
public interface FileInfosContract {
    interface View extends BaseView<FileInfosContractPresenter> {
        /**
         *
         */
        void showProgressDialog();

        /**
         *
         */
        void closeProgressDialog();

        void showNoFileInfos();

        void showFileInfos(List<FileInfo> fileInfos);
    }

    interface FileInfosContractPresenter extends BasePresenter {
//        /**
//         *
//         */
        void loadFileInfos();

        void init(FileInfosRepository fileInfosRepository, FileInfo fileInfo);
    }
}