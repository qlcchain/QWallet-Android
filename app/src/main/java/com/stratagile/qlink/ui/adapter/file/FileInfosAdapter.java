package com.stratagile.qlink.ui.adapter.file;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.data.fileInfo.FileInfo;

import java.util.List;

public class FileInfosAdapter extends BaseQuickAdapter<FileInfo, BaseViewHolder> {

    public FileInfosAdapter(@Nullable List<FileInfo> data) {
        super(R.layout.item_file_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileInfo item) {
        helper.setText(R.id.text_view_file_info_name, item.getName());
        if (item.isFile()) {
            helper.setImageResource(R.id.image_view_file_info_icon, R.drawable.ic_insert_drive_file_pink_a400_36dp);
        } else {
            helper.setImageResource(R.id.image_view_file_info_icon, R.drawable.ic_folder_light_blue_500_36dp);
        }
    }
}
