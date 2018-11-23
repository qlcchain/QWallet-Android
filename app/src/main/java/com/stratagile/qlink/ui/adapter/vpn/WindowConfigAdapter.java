package com.stratagile.qlink.ui.adapter.vpn;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.vpn.WindowConfig;

import java.util.List;


/**
 * @author zl
 * @Package com.stratagile.qlink.ui.adapter.vpn
 * @Description: $description  adapter of RegisteWindowVpnActivityActivity
 * @date 2018/08/10 15:08:22
 */

public class WindowConfigAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int selectItem = 0;

    private String fromType;

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    private OnItemChangeListener onItemChangeListener;

    public OnItemChangeListener getOnItemChangeListener() {
        return onItemChangeListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public WindowConfigAdapter(@Nullable List<String> data) {
        super(R.layout.item_config_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String item) {
        CheckBox checkBox = baseViewHolder.getView(R.id.checkbox);
        if (baseViewHolder.getLayoutPosition() == 0) {
            baseViewHolder.setVisible(R.id.line1, true);
        } else {
            baseViewHolder.setVisible(R.id.line1, false);
        }
        baseViewHolder.setText(R.id.publicAddress, item);
        if (baseViewHolder.getLayoutPosition() == selectItem) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem = baseViewHolder.getLayoutPosition();
                notifyDataSetChanged();
                if (onItemChangeListener != null) {
                    onItemChangeListener.onItemChange(selectItem);
                }
            }
        });
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (baseViewHolder.getLayoutPosition() == selectItem) {

                } else {
                    selectItem = baseViewHolder.getLayoutPosition();
                    notifyDataSetChanged();
                    if (onItemChangeListener != null) {
                        onItemChangeListener.onItemChange(selectItem);
                    }
//                    if ("worldCup".equals(fromType)) {
//                        if (item.getIsMain()) {
//                            selectItem = baseViewHolder.getLayoutPosition();
//                            notifyDataSetChanged();
//                            if (onItemChangeListener != null) {
//                                onItemChangeListener.onItemChange(selectItem);
//                            }
//                        } else {
//
//                        }
//                    } else {
//                        if (!item.getIsMain()) {
//                            selectItem = baseViewHolder.getLayoutPosition();
//                            notifyDataSetChanged();
//                            if (onItemChangeListener != null) {
//                                onItemChangeListener.onItemChange(selectItem);
//                            }
//                        } else {
//
//                        }
//                    }
                }
            }
        });
    }

    public interface OnItemChangeListener {
        void onItemChange(int position);
    }
}
